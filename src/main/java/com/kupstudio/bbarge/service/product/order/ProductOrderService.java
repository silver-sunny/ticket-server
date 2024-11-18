package com.kupstudio.bbarge.service.product.order;

import com.kupstudio.bbarge.constant.product.ProductOrderConstant;
import com.kupstudio.bbarge.dao.dbProduct.product.ProductOrderDao;
import com.kupstudio.bbarge.dto.pagination.PageHelperDto;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.product.order.*;
import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductOrderStatusTypeEnum;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.service.product.ProductService;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final ProductOrderDao productOrderDao;
    private final StoreService storeService;
    private final ProductService productService;

    // 상품 주문 내역 등록
    public void insertOrder(ProductOrderEntity entity) {
        productOrderDao.insertOrder(entity);
    }

    public ProductOrderEntity getOrder(int orderNo) {
        return productOrderDao.getOrder(orderNo);
    }

    public ProductOrderEntity getOrderAndEmptyCheck(int orderNo) {
        ProductOrderEntity productOrderEntity = productOrderDao.getOrder(orderNo);
        if (ObjectUtils.isEmpty(productOrderEntity)) {
            throw new PostNotExistException(ProductOrderConstant.ORDER_IS_NOT_EXIST);
        }
        return productOrderEntity;
    }

    public OrderDetailDto getOrderDetail(int orderNo) {
        return productOrderDao.getOrderDetail(orderNo);
    }

    public OrderDetailDto getOrderDetailWithChannelDetail(int orderNo) {
        OrderDetailDto orderDetailDto = getOrderDetail(orderNo);
        if (ObjectUtils.isEmpty(orderDetailDto)) {
            throw new PostNotExistException(ProductOrderConstant.ORDER_IS_NOT_EXIST);
        }

        ProductDto productDto = productService.getProductDetail(orderDetailDto.getProductNo());
        orderDetailDto.setChannelDetailDto(ChannelEnum.getChannelDetail(productDto));
        orderDetailDto.setProductName(productDto.getProductName());
        return orderDetailDto;
    }

    public boolean isExistOrderByOrderNo(int orderNo) {
        return productOrderDao.isExistOrderByOrderNo(orderNo);
    }

    public void isValidOrderNo(int orderNo) {
        // 없는 주문 번호일 경우 exception
        if (!isExistOrderByOrderNo(orderNo)) {
            throw new PostNotExistException(ProductOrderConstant.ORDER_IS_NOT_EXIST);
        }
    }

    // 특정 상품의 주문 내역이 있는지 확인
    public boolean isExistOrderByProductNo(int productNo) {
        return productOrderDao.isExistOrderByProductNo(productNo);
    }

    public Map<String, Object> getAllOrderList(OrderSearchDto orderSearchDto) {

        int total = productOrderDao.getAllOrderTotal(orderSearchDto);

        int countPerPage = orderSearchDto.getCountPerPage();
        int page = orderSearchDto.getPage();
        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("params", params);
            return result;
        }

        int offSet = (page - 1) * countPerPage;
        orderSearchDto.setOffSet(offSet);

        List<OrderDto> list = productOrderDao.getAllOrderList(orderSearchDto);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("params", params);
        return result;
    }

    public int getOrderNonIssuedTicketTotalCount() {
        OrderSearchDto orderSearchDto = new OrderSearchDto();
        orderSearchDto.setIsIssuance(true);
        orderSearchDto.setOrderStateNos(getOrderStateNosForNonIssuedTicket());
        orderSearchDto.setCancelOrReturnStateNos(getCancelOrReturnStateNosForNonIssuedTicket());

        return productOrderDao.getOrderNonIssuedTicketTotal(orderSearchDto);
    }

    public Map<String, Object> getOrderNonIssuedTicketList(OrderSearchDto orderSearchDto) {

        orderSearchDto.setOrderStateNos(getOrderStateNosForNonIssuedTicket());
        orderSearchDto.setCancelOrReturnStateNos(getCancelOrReturnStateNosForNonIssuedTicket());

        int total = productOrderDao.getOrderNonIssuedTicketTotal(orderSearchDto);

        int countPerPage = orderSearchDto.getCountPerPage();
        int page = orderSearchDto.getPage();
        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("params", params);
            return result;
        }

        int offSet = (page - 1) * countPerPage;
        orderSearchDto.setOffSet(offSet);

        List<OrderNonIssuedTicketDto> list = productOrderDao.getOrderNonIssuedTicketList(orderSearchDto);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("params", params);
        return result;
    }

    public Map<String, Object> getOrderCancelOrReturnRequestList(OrderSearchDto orderSearchDto) {

        orderSearchDto.setCancelOrReturnStateNos(getCancelOrReturnRequestStateNos());
        orderSearchDto.setCancelRequestStateNo(CancelOrReturnStateEnum.CANCEL_REQUEST.getStatusNo());
        orderSearchDto.setReturnRequestStateNo(CancelOrReturnStateEnum.RETURN_REQUEST.getStatusNo());

        int total = productOrderDao.getCancelOrReturnTotal(orderSearchDto);

        int countPerPage = orderSearchDto.getCountPerPage();
        int page = orderSearchDto.getPage();
        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("params", params);
            return result;
        }

        int offSet = (page - 1) * countPerPage;
        orderSearchDto.setOffSet(offSet);

        List<OrderCancelOrReturnDto> list = productOrderDao.getCancelOrReturnList(orderSearchDto);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("params", params);
        return result;
    }

    public Map<String, Object> getOrderCancelOrReturnDoneList(OrderSearchDto orderSearchDto) {

        orderSearchDto.setCancelOrReturnStateNos(getCancelOrReturnDoneStateNos());
        orderSearchDto.setCancelDoneStateNo(CancelOrReturnStateEnum.CANCEL_DONE.getStatusNo());
        orderSearchDto.setReturnDoneStateNo(CancelOrReturnStateEnum.RETURN_DONE.getStatusNo());

        int total = productOrderDao.getCancelOrReturnTotal(orderSearchDto);

        int countPerPage = orderSearchDto.getCountPerPage();
        int page = orderSearchDto.getPage();
        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("params", params);
            return result;
        }

        int offSet = (page - 1) * countPerPage;
        orderSearchDto.setOffSet(offSet);

        List<OrderCancelOrReturnDto> list = productOrderDao.getCancelOrReturnList(orderSearchDto);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("params", params);
        return result;
    }

    // 미발급티켓 주문 상태 필터
    private List<Integer> getOrderStateNosForNonIssuedTicket() {
        List<Integer> orderStateNos = new ArrayList<>();

        orderStateNos.add(ProductOrderStatusTypeEnum.PAYED.getIndex());
        orderStateNos.add(ProductOrderStatusTypeEnum.PURCHASE_DECIDED.getIndex());

        return orderStateNos;
    }

    // 미발급티켓 취소/반품 상태 필터
    private List<Integer> getCancelOrReturnStateNosForNonIssuedTicket() {

        List<Integer> cancelOrReturnStateNos = new ArrayList<>();

        cancelOrReturnStateNos.add(CancelOrReturnStateEnum.ETC.getStatusNo());
        cancelOrReturnStateNos.add(CancelOrReturnStateEnum.CANCEL_REJECT.getStatusNo());
        cancelOrReturnStateNos.add(CancelOrReturnStateEnum.RETURN_REJECT.getStatusNo());
        cancelOrReturnStateNos.add(CancelOrReturnStateEnum.ADMIN_CANCEL_REJECT.getStatusNo());

        return cancelOrReturnStateNos;
    }

    // 취소/반품 요청 필터
    private List<Integer> getCancelOrReturnRequestStateNos() {

        List<Integer> requestStateNos = new ArrayList<>();

        requestStateNos.add(CancelOrReturnStateEnum.CANCEL_REQUEST.getStatusNo());
        requestStateNos.add(CancelOrReturnStateEnum.RETURN_REQUEST.getStatusNo());

        return requestStateNos;
    }

    // 취소/반품 처리완료 필터
    private List<Integer> getCancelOrReturnDoneStateNos() {

        List<Integer> doneStateNos = new ArrayList<>();

        doneStateNos.add(CancelOrReturnStateEnum.CANCEL_DONE.getStatusNo());
        doneStateNos.add(CancelOrReturnStateEnum.RETURN_DONE.getStatusNo());

        return doneStateNos;
    }

    public Map<String, Integer> getOrderCancelOrReturnTotalCount(Set<Integer> adminStoreNos) {
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("requestTotalCount", productOrderDao.getCancelOrReturnTotalByStateNos(adminStoreNos, getCancelOrReturnRequestStateNos()));
        resultMap.put("doneTotalCount", productOrderDao.getCancelOrReturnTotalByStateNos(adminStoreNos, getCancelOrReturnDoneStateNos()));
        return resultMap;
    }

    /**
     * 처리중 활성화
     * 상태 변경되면 productOrder에 처리중으로 (is_progressing = 1) 로 변경
     * bbarge batch 돌릴때 소셜에서 상태 변경된 값 가져오면 처리중 X (is_progressing = 0) 으로 변경
     *
     * @param channelProductOrderId
     * @param channel
     */
    public void updateProductOrderIsProgressing(String channelProductOrderId,
                                                ChannelEnum channel) {
        productOrderDao.updateProductOrderIsProgressing(channelProductOrderId, channel.getIndex());

    }

    public void updateProductOrderIsIssuance(int orderNo) {

        productOrderDao.updateProductOrderIsIssuance(orderNo);
    }

    public void updateProductOrderStateNo(int orderNo, ProductOrderStatusTypeEnum statusTypeEnum) {

        productOrderDao.updateProductOrderStateNo(orderNo, statusTypeEnum.getIndex());
    }

    public void updateProductOrderCancelOrReturnState(int orderNo, String rejectReturnReason, CancelOrReturnStateEnum cancelOrReturnStateEnum) {

        productOrderDao.updateProductOrderCancelOrReturnState(orderNo, rejectReturnReason, cancelOrReturnStateEnum.getStatusNo());
    }

}
