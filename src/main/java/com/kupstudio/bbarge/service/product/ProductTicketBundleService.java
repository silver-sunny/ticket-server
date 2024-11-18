package com.kupstudio.bbarge.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupstudio.bbarge.apiDao.SmsApiDao;
import com.kupstudio.bbarge.constant.common.CommonConstant;
import com.kupstudio.bbarge.constant.product.ProductTicketConstant;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.product.ProductTicketInfoDto;
import com.kupstudio.bbarge.dto.product.ProductTicketSearchDto;
import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.dto.sms.MessageDto;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import com.kupstudio.bbarge.enumClass.product.ProductOrderStatusTypeEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.exception.common.SmsNotValidException;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.product.order.ProductOrderService;
import com.kupstudio.bbarge.service.store.StoreService;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import com.kupstudio.bbarge.utils.TicketKeyGeneratorUtil;
import com.kupstudio.bbarge.validation.validator.sms.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.kupstudio.bbarge.constant.product.ProductTicketConstant.*;

@Service
@RequiredArgsConstructor
public class ProductTicketBundleService {

    private final ProductTicketService productTicketService;

    private final ProductService productService;

    private final StoreService storeService;

    private final ProductOrderService productOrderService;

    private final AdminService adminService;

    private final SmsApiDao smsApiDao;

    @Value("${web.server.url}")
    private String webServerUrl;

    public List<String> insertProductTicket(int orderNo) throws ConditionFailException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {

        ProductOrderEntity productOrder = productOrderService.getOrder(orderNo);

        // 자체에서가 아니면 이 과정으로 발급 못함.
        if (ChannelEnum.getEnumOfIndex(productOrder.getChannelNo()) != ChannelEnum.NEVERLAND_HC) {
            throw new ConditionFailException(NOT_ALLOWED_CHANNEL);
        }

        // isProgressing 이면 발급 못함
        if (productOrder.getIsProgressing()) {
            throw new ConditionFailException(IN_PROGRESS_TICKET);
        }

        // 상품이 있는지 확인 없으면 PostNotExistException()
        productService.isValidProduct(productOrder.getProductNo());

        List<String> ticketKeyList = TicketKeyGeneratorUtil.generateTicketKey(productOrder.getPurchaseQuantity());
        List<ProductTicketEntity> productTicketEntityList = new ArrayList<>();
        for (String ticketKey : ticketKeyList) {
            ProductTicketEntity productTicketEntity = ProductTicketEntity.ticketEntityByProductOrder(productOrder, ticketKey);
            productTicketEntityList.add(productTicketEntity);
        }

        insertTicketAndSendMessage(productOrder, productTicketEntityList);

        return ticketKeyList;
    }

    /**
     * 티켓을 넣고 상태값을 업데이트
     */
    void insertTicketAndSendMessage(ProductOrderEntity productOrder, List<ProductTicketEntity> productTicketEntityList) throws NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        int orderNo = productOrder.getOrderNo();

        productTicketService.insertProductTicketList(productTicketEntityList);
        productOrderService.updateProductOrderIsIssuance(orderNo);
//        발급 완료 시 order 상태값 DELIVERING 으로 변경
//        productOrderService.updateProductOrderStateNo(orderNo, ProductOrderStatusTypeEnum.DELIVERING);
        sendTicketMessage(productOrder);
    }

    /**
     * 문자 발송 후 상태값 업데이트
     */
    public void sendTicketMessage(ProductOrderEntity productOrder) throws NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        int orderNo = productOrder.getOrderNo();

        // 구매내역의 전화번호가 유효하지 않다면 exception
        if (!PhoneNumberValidator.isValidPhoneNumber(productOrder.getPhoneNumber())) {
            throw new SmsNotValidException(CommonConstant.SMS_TO_IS_NOT_VALID);
        }
        String messageContent = webServerUrl + "/m.ticket/" + orderNo;
        smsApiDao.sendMessage(MessageDto.builder().to(productOrder.getPhoneNumber()).content(messageContent).build());
        // 발급 완료 시 order 상태값 DELIVERED 로 변경
        productOrderService.updateProductOrderStateNo(orderNo, ProductOrderStatusTypeEnum.DELIVERED);
    }


    public ProductTicketInfoDto getProductTicketInfo(String ticketKey) {

        ProductTicketEntity productTicketEntity = productTicketService.getProductTicketByTicketKeyAndExistCheck(ticketKey);

        ProductDto productDto = productService.getProductDetail(productTicketEntity.getProductNo());
        StoreDto storeDto = storeService.getStoreDetail(productTicketEntity.getStoreNo());

        return new ProductTicketInfoDto(productTicketEntity, productDto, storeDto);
    }


    public ProductTicketSearchDto getProductTicketSearch(String ticketKey) {
        ProductTicketEntity productTicketEntity = productTicketService.getProductTicketByTicketKeyAndExistCheck(ticketKey);

        OrderDetailDto orderDetailDto = productOrderService.getOrderDetail(productTicketEntity.getOrderNo());
        String productName = productService.getProductName(productTicketEntity.getProductNo());
        String storeName = storeService.getStoreName(productTicketEntity.getStoreNo());

        return new ProductTicketSearchDto(productTicketEntity, orderDetailDto, productName, storeName);
    }

    public PagingResponse getProductTicketList(DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        if (StringUtils.isNotEmpty(searchWord)) {
            searchWord = EmojiRemoveUtil.removeEmojiForFilter(searchWord);
        } else {
            detailedConditionEnum = null;
        }

        // 티켓 키로 검색 시 다른 티켓 번호도 나열
        if (detailedConditionEnum != null && detailedConditionEnum.equals(DetailedConditionEnum.TICKET_KEY)) {
            return productTicketService.getProductTicketListByOrderNo(searchWord, countPerPage, page);
        }

        return productTicketService.getProductTicketList(detailedConditionEnum, searchWord, countPerPage, page);
    }

    public void updateProductTicketUsed(List<String> ticketKeys) throws PostNotExistException, ConditionFailException {

        List<Integer> orderNoList = new ArrayList<>();

        for (String ticketKey : ticketKeys) {
            ProductTicketEntity productTicketEntity = productTicketService.getProductTicketByTicketKey(ticketKey);
            if (productTicketEntity == null) {
                throw new PostNotExistException(ticketKey + " " + ProductTicketConstant.NOT_EXIST_TICKET);
            }
            if (productTicketEntity.isUsed()) {
                throw new ConditionFailException(ticketKey + " " + ALREADY_USED_TICKET);
            }

            ProductOrderEntity productOrderEntity = productOrderService.getOrder(productTicketEntity.getOrderNo());
            if (productOrderEntity.getIsProgressing()) {
                throw new ConditionFailException(ticketKey + " " + IN_PROGRESS_TICKET);
            }

            String productOrderState = ProductOrderStatusTypeEnum.getEnumOfIndex(productOrderEntity.getOrderStateNo()).name();
            if (!StringUtils.equalsAny(
                    productOrderState,
                    ProductOrderStatusTypeEnum.PURCHASE_DECIDED.name(),
                    ProductOrderStatusTypeEnum.DELIVERED.name())) {
                throw new ConditionFailException(ticketKey + " " + ORDER_STATE_ERROR_MESSAGE);
            }

            CancelOrReturnStateEnum cancelOrReturnState = CancelOrReturnStateEnum.getEnumOfStatusNo(productOrderEntity.getCancelOrReturnStateNo());
            if (!StringUtils.equalsAny(
                    cancelOrReturnState.name(),
                    CancelOrReturnStateEnum.ETC.name(),
                    CancelOrReturnStateEnum.CANCEL_REJECT.name(),
                    CancelOrReturnStateEnum.RETURN_REQUEST.name(),
                    CancelOrReturnStateEnum.RETURN_REJECT.name(),
                    CancelOrReturnStateEnum.PURCHASE_DECISION_REQUEST.name(),
                    CancelOrReturnStateEnum.PURCHASE_DECISION_HOLDBACK_RELEASE.name(),
                    CancelOrReturnStateEnum.PURCHASE_DECISION_HOLDBACK.name(),
                    CancelOrReturnStateEnum.ADMIN_CANCEL_REJECT.name()
            )) {
                throw new ConditionFailException(ticketKey + " " + REQUESTED_CANCEL_OR_RETURN_TICKET_ERROR_MESSAGE + " 취소/반품 상태 : " + cancelOrReturnState.getMeaning());
            }

            if (!orderNoList.contains(productTicketEntity.getOrderNo())) {
                orderNoList.add(productTicketEntity.getOrderNo());
            }
        }
        productTicketService.updateProductTicketUsed(ticketKeys, adminService.getAuthenticatedAdminNo());
    }
}
