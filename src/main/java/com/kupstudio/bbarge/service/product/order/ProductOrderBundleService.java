package com.kupstudio.bbarge.service.product.order;

import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.dao.naver.product.NaverProductStateDao;
import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.dto.admin.AdminDto;
import com.kupstudio.bbarge.dto.pagination.PageHelperDto;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.product.order.OrderCancelOrReturnDto;
import com.kupstudio.bbarge.dto.product.order.OrderDto;
import com.kupstudio.bbarge.dto.product.order.OrderNonIssuedTicketDto;
import com.kupstudio.bbarge.dto.product.order.OrderSearchDto;
import com.kupstudio.bbarge.dto.user.UserInfoDto;
import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.enumClass.product.*;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import com.kupstudio.bbarge.service.admin.AdminBundleService;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.admin.AdminStoreService;
import com.kupstudio.bbarge.service.product.ProductService;
import com.kupstudio.bbarge.service.product.ProductTicketService;
import com.kupstudio.bbarge.service.store.StoreService;
import com.kupstudio.bbarge.service.user.UserService;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductOrderBundleService {

    private final UserService userService;
    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final NaverProductStateDao naverProductStateDao;
    private final AdminBundleService adminBundleService;
    private final AdminService adminService;
    private final AdminStoreService adminStoreService;
    private final StoreService storeService;
    private final ProductTicketService productTicketService;


    public int insertOrder(ProductOrderRequestDto requestDto) {

        String directOption = requestDto.getDirectOption();
        if (StringUtils.isNotBlank(directOption)) {
            EmojiRemoveUtil.removeEmoji(directOption);
        } else {
            directOption = null;
        }

        // 유효한 상품인지 확인
        ProductDto productDto = productService.getProductDetail(requestDto.getProductNo());
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
        }

        UserInfoDto userInfoDto = userService.getUserInfo(requestDto.getUserNo());

        ProductOrderEntity entity = ProductOrderEntity.ProductOrderRequestDtToProductOrderEntity(directOption, requestDto, productDto, userInfoDto);

        // 주문 내역에 추가
        productOrderService.insertOrder(entity);

        return entity.getOrderNo();
    }

    // 전체 주문 내역 리스트 조회
    public PagingResponse getAllOrderList(Integer storeNo, InquiryPeriodEnum inquiryPeriodEnum, LocalDate startDate, LocalDate endDate, ProductOrderFilterEnum productOrderFilterEnum, CancelOrReturnFilterEnum cancelOrReturnFilterEnum, DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();
        // 검색 필터 -> [판매 매장] 값이 있을 경우 로그인한 운영자가 해당 매장을 담당 중인지 체크;
        if (storeNo != null) {
            adminBundleService.isValidAdminStoreNo(storeNo);
        }

        OrderSearchDto orderSearchDto = new OrderSearchDto().paramsToOrderSearchDto(storeNo, inquiryPeriodEnum, startDate, endDate, productOrderFilterEnum, null, cancelOrReturnFilterEnum, detailedConditionEnum, searchWord, countPerPage, page);

        Map<String, Object> result = productOrderService.getAllOrderList(getDefaultStoreNos(orderSearchDto));
        List<OrderDto> list = (List<OrderDto>) result.get("list");
        PageHelperDto params = (PageHelperDto) result.get("params");

        for (OrderDto dto : list) {
            ProductDto productDto = productService.getProductDetail(dto.getProductNo());
            dto.setChannelDetailDto(ChannelEnum.getChannelDetail(productDto));
            dto.setProductName(productDto.getProductName());
            dto.setStoreName(storeService.getStoreName(dto.getStoreNo()));
            if (dto.isIssuance()) {
                dto.setTicketStateEnum(getTicketStateEnum(dto.getOrderNo()));
            } else {
                dto.setTicketStateEnum(TicketStateEnum.NON_ISSUED);
            }
        }

        return new PagingResponse(list, params);
    }

    // 미발급티켓 리스트 조회
    public PagingResponse getOrderNonIssuedTicketList(Integer storeNo, InquiryPeriodEnum inquiryPeriodEnum, LocalDate startDate, LocalDate endDate, DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();
        // 검색 필터 -> [판매 매장] 값이 있을 경우 로그인한 운영자가 해당 매장을 담당 중인지 체크;
        if (storeNo != null) {
            adminBundleService.isValidAdminStoreNo(storeNo);
        }

        OrderSearchDto orderSearchDto = new OrderSearchDto().paramsToOrderSearchDto(storeNo, inquiryPeriodEnum, startDate, endDate, null, false, null, detailedConditionEnum, searchWord, countPerPage, page);

        Map<String, Object> result = productOrderService.getOrderNonIssuedTicketList(getDefaultStoreNos(orderSearchDto));
        List<OrderNonIssuedTicketDto> list = (List<OrderNonIssuedTicketDto>) result.get("list");
        PageHelperDto params = (PageHelperDto) result.get("params");

        for (OrderNonIssuedTicketDto dto : list) {
            ProductDto productDto = productService.getProductDetail(dto.getProductNo());
            dto.setChannelDetailDto(ChannelEnum.getChannelDetail(productDto));
            dto.setProductName(productDto.getProductName());
            dto.setStoreName(storeService.getStoreName(dto.getStoreNo()));
            if (dto.isIssuance()) {
                dto.setTicketStateEnum(getTicketStateEnum(dto.getOrderNo()));
            } else {
                dto.setTicketStateEnum(TicketStateEnum.NON_ISSUED);
            }
        }

        return new PagingResponse(list, params);
    }

    // 취소/반품 요청, 처리완료 총 개수
    public Map<String, Integer> getOrderCancelOrReturnTotalCount() {

        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();

        AdminAuthDto adminInfo = adminService.getLoginAdminInfo();

        Set<Integer> adminStoreNos = new HashSet<>();
        switch (adminInfo.getRole()) {
            case ROLE_MIDDLE:
            case ROLE_STORE:
                // 중간, 하위 운영자의 담당 매장 번호 리스트 셋팅
                adminStoreNos = adminStoreService.getAdminStoreListByAdminNo(adminInfo.getAdminNo());
                break;
            default:
                // 루트 운영자의 경우 전체 매장에 권한이 있으므로 따로 셋팅하지 않습니다.
                break;
        }

        return productOrderService.getOrderCancelOrReturnTotalCount(adminStoreNos);
    }

    // 취소/반품 요청 리스트 조회
    public PagingResponse getOrderCancelOrReturnRequestList(Integer storeNo, InquiryPeriodEnum inquiryPeriodEnum, LocalDate startDate, LocalDate endDate, DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();
        // 검색 필터 -> [판매 매장] 값이 있을 경우 로그인한 운영자가 해당 매장을 담당 중인지 체크;
        if (storeNo != null) {
            adminBundleService.isValidAdminStoreNo(storeNo);
        }

        OrderSearchDto orderSearchDto = new OrderSearchDto().paramsToOrderSearchDto(storeNo, inquiryPeriodEnum, startDate, endDate, null, null, null, detailedConditionEnum, searchWord, countPerPage, page);

        Map<String, Object> result = productOrderService.getOrderCancelOrReturnRequestList(getDefaultStoreNos(orderSearchDto));
        List<OrderCancelOrReturnDto> list = (List<OrderCancelOrReturnDto>) result.get("list");
        PageHelperDto params = (PageHelperDto) result.get("params");

        for (OrderCancelOrReturnDto dto : list) {
            ProductDto productDto = productService.getProductDetail(dto.getProductNo());
            dto.setChannelDetailDto(ChannelEnum.getChannelDetail(productDto));
            dto.setProductName(productDto.getProductName());
            dto.setStoreName(storeService.getStoreName(dto.getStoreNo()));
            if (dto.isIssuance()) {
                dto.setTicketStateEnum(getTicketStateEnum(dto.getOrderNo()));
            } else {
                dto.setTicketStateEnum(TicketStateEnum.NON_ISSUED);
            }
        }

        return new PagingResponse(list, params);
    }

    // 취소/반품 처리완료 리스트 조회
    public PagingResponse getOrderCancelOrReturnDoneList(Integer storeNo, InquiryPeriodEnum inquiryPeriodEnum, LocalDate startDate, LocalDate endDate, DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();
        // 검색 필터 -> [판매 매장] 값이 있을 경우 로그인한 운영자가 해당 매장을 담당 중인지 체크;
        if (storeNo != null) {
            adminBundleService.isValidAdminStoreNo(storeNo);
        }

        OrderSearchDto orderSearchDto = new OrderSearchDto().paramsToOrderSearchDto(storeNo, inquiryPeriodEnum, startDate, endDate, null, null, null, detailedConditionEnum, searchWord, countPerPage, page);

        Map<String, Object> result = productOrderService.getOrderCancelOrReturnDoneList(getDefaultStoreNos(orderSearchDto));
        List<OrderCancelOrReturnDto> list = (List<OrderCancelOrReturnDto>) result.get("list");
        PageHelperDto params = (PageHelperDto) result.get("params");

        for (OrderCancelOrReturnDto dto : list) {
            ProductDto productDto = productService.getProductDetail(dto.getProductNo());
            dto.setChannelDetailDto(ChannelEnum.getChannelDetail(productDto));
            dto.setProductName(productDto.getProductName());
            dto.setStoreName(storeService.getStoreName(dto.getStoreNo()));
            if (dto.isIssuance()) {
                dto.setTicketStateEnum(getTicketStateEnum(dto.getOrderNo()));
            } else {
                dto.setTicketStateEnum(TicketStateEnum.NON_ISSUED);
            }
        }


        return new PagingResponse(list, params);
    }

    public void returnRejectOrder(int orderNo, String rejectReturnReason) {

        ProductOrderEntity entity = productOrderService.getOrderAndEmptyCheck(orderNo);

        if (entity.getChannelNo() == ChannelEnum.NEVERLAND_HC.getIndex()) {
            productOrderService.updateProductOrderCancelOrReturnState(orderNo, rejectReturnReason, CancelOrReturnStateEnum.RETURN_REJECT);
        } else if (entity.getChannelNo() == ChannelEnum.NAVER.getIndex()) {
            naverProductStateDao.naverReturnReject(orderNo, rejectReturnReason);
        }

    }

    // 주문관리 로그인한 운영자별 기본 매장 셋팅
    private OrderSearchDto getDefaultStoreNos(OrderSearchDto orderSearchDto) {

        // 판매매장 필터 값이 있다면 셋팅 안함
        if (orderSearchDto.getStoreNo() != null) {
            return orderSearchDto;
        }

        // 판매매장 필터 값이 없다면 로그인한 운영자의 담당 매장만 보이도록 셋팅
        int adminNo = adminService.getAuthenticatedAdminNo();
        AdminDto adminInfo = adminService.getAdmin(adminNo);
        AdminRoleEnum role = adminInfo.getRole();

        switch (role) {
            case ROLE_MIDDLE:
            case ROLE_STORE:
                // 중간, 하위 운영자의 담당 매장 번호 리스트 셋팅
                orderSearchDto.setAdminStoreNos(adminStoreService.getAdminStoreListByAdminNo(adminNo));
                break;
            default:
                // 루트 운영자의 경우 전체 매장에 권한이 있으므로 따로 셋팅하지 않습니다.
                break;
        }

        return orderSearchDto;
    }

    private TicketStateEnum getTicketStateEnum(int orderNo) {
        List<ProductTicketEntity> ticketList = productTicketService.getProductTicketListByOrderNo(orderNo);

        // 사용된 티켓 개수 count
        long usedCount = ticketList.stream()
                .filter(ProductTicketEntity::isUsed)
                .count();

        if (usedCount == ticketList.size()) {
            // 사용된 티켓이 발급된 티켓의 수와 같다면
            return TicketStateEnum.ALL_USED;
        } else if (usedCount == 0) {
            // 사용된 티켓이 없다면
            return TicketStateEnum.AVAILABLE;
        } else {
            // 일부 티켓만 사용 되었다면
            return TicketStateEnum.SOME_USED;
        }
    }
}
