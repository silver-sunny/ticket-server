package com.kupstudio.bbarge.dto.product.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kupstudio.bbarge.dto.product.ChannelDetailDto;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
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
public class OrderCancelOrReturnDto {

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

    @Schema(description = "취소/반품상태")
    private CancelOrReturnStateEnum cancelOrReturnStateEnum;

    @Schema(description = "주문일시")
    private LocalDateTime purchaseAt;

    @Schema(description = "취소/반품 요청일시")
    private String cancelOrReturnRequestAt;

    @Schema(description = "취소/반품 처리일시")
    private String cancelOrReturnProcessingAt;

    @Schema(description = "취소/반품요청사유")
    private String cancelOrReturnReason;

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

    @Schema(description = "티켓상태")
    private TicketStateEnum ticketStateEnum;

    @Schema(description = "티켓 발급 여부")
    private boolean isIssuance;

    @JsonIgnore // 취소 요청일
    private String cancelRequestAt;

    @JsonIgnore // 취소 철회일
    private String cancelRejectAt;

    @JsonIgnore // 취소 완료일
    private String cancelDoneAt;

    @JsonIgnore // 반품 요청일
    private String returnRequestAt;

    @JsonIgnore // 반품 철회일
    private String returnRejectAt;

    @JsonIgnore // 반품 완료일
    private String returnDoneAt;

    @Schema(description = "주문상태 번호")
    public int getProductOrderStateNo() {
        return productOrderStatusTypeEnum.getIndex();
    }

    @Schema(description = "주문상태 이름")
    public String getProductOrderStateName() {
        return productOrderStatusTypeEnum.getMeaning();
    }

    public void setOrderStateNo(int stateNo) {
        productOrderStatusTypeEnum = ProductOrderStatusTypeEnum.getEnumOfIndex(stateNo);
    }

    @Schema(description = "취소/반품상태 번호")
    public Integer getCancelOrReturnStateNo() {
        return cancelOrReturnStateEnum.getStatusNo();
    }

    public void setCancelOrReturnStateNo(int stateNo) {
        cancelOrReturnStateEnum = CancelOrReturnStateEnum.getEnumOfStatusNo(stateNo);
    }

    @Schema(description = "취소/반품상태 이름")
    public String getCancelOrReturnStateName() {
        return cancelOrReturnStateEnum.getMeaning();
    }

    @Schema(description = "티켓상태 번호")
    public Integer getTicketStateNo() {
        if (ticketStateEnum != null) {
            return ticketStateEnum.getStateNo();
        }
        return null;
    }

    @Schema(description = "티켓상태 이름")
    public String getTicketStateName() {
        if (ticketStateEnum != null) {
            return ticketStateEnum.getMeaning();
        }
        return null;
    }

    public void setChannelNo(int index) {
        channel = ChannelEnum.getEnumOfIndex(index);
    }

    public String getCancelOrReturnRequestAt() {
        switch (cancelOrReturnStateEnum) {
            case CANCEL_REQUEST:
            case CANCEL_REJECT:
            case CANCEL_DONE:
                return cancelRequestAt;
            case RETURN_REQUEST:
            case RETURN_REJECT:
            case RETURN_DONE:
                return returnRequestAt;
        }
        return null;
    }

    public String getCancelOrReturnProcessingAt() {
        switch (cancelOrReturnStateEnum) {
            case CANCEL_REQUEST:
            case RETURN_REQUEST:
                return null;
            case CANCEL_REJECT:
                return cancelRejectAt;
            case CANCEL_DONE:
                return cancelDoneAt;
            case RETURN_REJECT:
                return returnRejectAt;
            case RETURN_DONE:
                return returnDoneAt;
        }
        return null;
    }
}
