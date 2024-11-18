package com.kupstudio.bbarge.dto.product.order;

import com.kupstudio.bbarge.dto.product.ChannelDetailDto;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductOrderStatusTypeEnum;
import com.kupstudio.bbarge.enumClass.product.TicketStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderNonIssuedTicketDto {

    @Schema(description = "주문번호")
    private int orderNo;

    @Schema(description = "구매채널")
    private ChannelEnum channel;

    @Schema(description = "구매채널 상세정보")
    private ChannelDetailDto channelDetailDto;

    @Schema(description = "소셜주문번호")
    private String channelOrderId;

    @Schema(description = "옵션(소셜상품)주문번호")
    private String channelProductOrderId;

    @Schema(description = "주문상태")
    private ProductOrderStatusTypeEnum productOrderStatusTypeEnum;

    @Schema(description = "티켓상태")
    private TicketStateEnum ticketStateEnum;

    @Schema(description = "주문일시")
    private LocalDateTime purchaseAt;

    @Schema(description = "발급기한")
    private LocalDateTime shippingDueDate;

    @Schema(description = "매장번호")
    private int storeNo;

    @Schema(description = "판매매장")
    private String storeName;

    @Schema(description = "상품번호")
    private int productNo;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Schema(description = "수량")
    private int purchaseQuantity;

    @Schema(description = "구매자명")
    private String purchaseUserName;

    @Schema(description = "구매자연락처")
    private String phoneNumber;

    @Schema(description = "구매자ID")
    private String purchaseUserId;

    @Schema(description = "처리중 여부")
    private boolean isProgressing;

    @Schema(description = "티켓 발급 여부")
    private boolean isIssuance;

    @Schema(description = "주문상태 번호")
    public int getProductOrderStateNo() {
        return productOrderStatusTypeEnum.getIndex();
    }

    @Schema(description = "주문상태 이름")
    public String getProductOrderStateName() {
        return productOrderStatusTypeEnum.getMeaning();
    }

    @Schema(description = "티켓상태 번호")
    public int getTicketStateNo() {
        return ticketStateEnum.getStateNo();
    }

    @Schema(description = "티켓상태 이름")
    public String getTicketStateName() {
        return ticketStateEnum.getMeaning();
    }

    public void setOrderStateNo(int stateNo) {
        productOrderStatusTypeEnum = ProductOrderStatusTypeEnum.getEnumOfIndex(stateNo);
    }

    public void setChannelNo(int index) {
        channel = ChannelEnum.getEnumOfIndex(index);
    }
}

