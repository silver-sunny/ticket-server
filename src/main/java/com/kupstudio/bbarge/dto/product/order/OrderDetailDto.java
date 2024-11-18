package com.kupstudio.bbarge.dto.product.order;

import com.kupstudio.bbarge.dto.product.ChannelDetailDto;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.TicketStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDto {

    @Schema(description = "주문번호")
    private int orderNo;

    @Schema(description = "소셜주문번호")
    private String channelOrderId;

    @Schema(description = "구매채널")
    private ChannelEnum channel;

    @Schema(description = "구매채널 상세정보")
    private ChannelDetailDto channelDetailDto;

    @Schema(description = "옵션(소셜상품)주문번호")
    private String channelProductOrderId;

    @Schema(description = "주문상태")
    private TicketStateEnum ticketStateEnum;

    @Schema(description = "취소/반품상태")
    private CancelOrReturnStateEnum cancelOrReturnStateEnum;

    @Schema(description = "매장번호")
    private int storeNo;

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

    @Schema(description = "주문상태 번호")
    public int getTicketStateNo() {
        return ticketStateEnum.getStateNo();
    }

    @Schema(description = "주문상태 이름")
    public String getTicketStateName() {
        return ticketStateEnum.getMeaning();
    }


    public void setTicketStateNo(int stateNo) {
        ticketStateEnum = TicketStateEnum.getEnumOfStateNo(stateNo);
    }

    @Schema(description = "취소/반품상태 번호")
    public Integer getCancelOrReturnStateNo() {
        if (cancelOrReturnStateEnum != null) {
            return cancelOrReturnStateEnum.getStatusNo();
        }
        return null;
    }

    @Schema(description = "취소/반품상태 이름")
    public String getCancelOrReturnStateName() {
        if (cancelOrReturnStateEnum != null) {
            return cancelOrReturnStateEnum.getMeaning();
        }
        return null;
    }

    public void setCancelOrReturnStateNo(Integer stateNo) {
        cancelOrReturnStateEnum = CancelOrReturnStateEnum.getEnumOfStatusNo(stateNo);
    }

    public void setChannelNo(int index) {
        channel = ChannelEnum.getEnumOfIndex(index);
    }

}
