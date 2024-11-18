package com.kupstudio.bbarge.dao.dbProduct.product;

import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductTicketDao {


    ProductTicketEntity getProductTicketByTicketKey(String ticketKey);

    void insertTicket(ProductTicketEntity productTicketEntity);

    void insertTicketList(List<ProductTicketEntity> productTicketEntityList);

    void updateProductTicketUsed(@Param(value = "ticketKeys") List<String> ticketKeys,
                                 @Param(value = "adminNo") int adminNo);


    int getTotal(@Param(value = "detailedConditionEnum") DetailedConditionEnum detailedConditionEnum,
                 @Param(value = "searchWord") String searchWord);

    List<ProductTicketEntity> getProductTicketList(@Param(value = "detailedConditionEnum") DetailedConditionEnum detailedConditionEnum,
                                                   @Param(value = "searchWord") String searchWord,
                                                   @Param(value = "countPerPage") int countPerPage,
                                                   @Param(value = "offSet") int offSet);

    String getChannelOrderId(String ticketKey);

    int getTotalByTicketKeyAndOrderNo(@Param(value = "orderNo") int orderNo);

    List<ProductTicketEntity> getProductTicketListByTicketKeyAndOrderNo(@Param(value = "ticketKey") String ticketKey,
                                                                        @Param(value = "orderNo") int orderNo,
                                                                        @Param(value = "countPerPage") int countPerPage,
                                                                        @Param(value = "offSet") int offSet);

    boolean isAllTicketByOrderUsed(int orderNo);

    void deleteProductTicketByOrder(int orderNo);

    List<ProductTicketEntity> getProductTicketListByOrderNo(@Param(value = "orderNo") int orderNo);
    
    /**
     * 주문번호 , 사용여부 에관련된 티켓 갯수
     *
     * @param orderNo
     * @param isUsed
     * @return
     */
    int countProductTicketUsedByOrderNo(@Param("orderNo") int orderNo,
                                        @Param("isUsed") boolean isUsed);
}
