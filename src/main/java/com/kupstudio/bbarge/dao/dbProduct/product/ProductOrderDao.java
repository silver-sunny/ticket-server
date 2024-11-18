package com.kupstudio.bbarge.dao.dbProduct.product;

import com.kupstudio.bbarge.dto.product.order.*;
import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductOrderDao {

    void insertOrder(ProductOrderEntity entity);

    ProductOrderEntity getOrder(@Param(value = "orderNo") int orderNo);

    OrderDetailDto getOrderDetail(@Param(value = "orderNo") int orderNo);

    boolean isExistOrderByOrderNo(@Param(value = "orderNo") int orderNo);

    boolean isExistOrderByProductNo(@Param(value = "productNo") int productNo);

    int getAllOrderTotal(OrderSearchDto orderSearchDto);

    List<OrderDto> getAllOrderList(OrderSearchDto orderSearchDto);

    int getOrderNonIssuedTicketTotal(OrderSearchDto orderSearchDto);

    List<OrderNonIssuedTicketDto> getOrderNonIssuedTicketList(OrderSearchDto orderSearchDto);

    int getCancelOrReturnTotal(OrderSearchDto orderSearchDto);

    List<OrderCancelOrReturnDto> getCancelOrReturnList(OrderSearchDto orderSearchDto);

    int getCancelOrReturnTotalByStateNos(@Param(value = "adminStoreNos") Set<Integer> adminStoreNos,
                                         @Param(value = "cancelOrReturnStateNos") List<Integer> cancelOrReturnStateNos);

    void updateProductOrderIsProgressing(@Param(value = "channelProductOrderId") String channelProductOrderId,
                                         @Param(value = "channelNo") int channelNo);

    void updateProductOrderIsIssuance(@Param(value = "orderNo") int orderNo);

    void updateProductOrderStateNo(@Param(value = "orderNo") int orderNo,
                                   @Param(value = "orderStateNo") int orderStateNo);

    void updateProductOrderCancelOrReturnState(@Param(value = "orderNo") int orderNo,
                                               @Param(value = "rejectReturnReason") String rejectReturnReason,
                                               @Param(value = "cancelOrReturnStateNo") int cancelOrReturnStateNo);

}
