package com.kupstudio.bbarge.dto.product;

import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.TicketStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "티켓 정보 DTO")
public class ProductTicketSearchDto {

    @Schema(description = "구매채널")
    private ChannelEnum channel;

    @Schema(description = "소셜주문번호")
    private String channelProductId;

    @Schema(description = "티켓 키")
    private String ticketKey;

    @Schema(description = "티켓상태")
    private boolean isUsed;

    @Schema(description = "티켓사용일시")
    private LocalDateTime usedAt;

    @Schema(description = "주문상태")
    private TicketStateEnum ticketStateEnum;

    @Schema(description = "취소/반품상태")
    private CancelOrReturnStateEnum cancelOrReturnStateEnum;


    @Schema(description = "구매자명")
    private String purchaseUserName;

    @Schema(description = "구매자연락처")
    private String phoneNumber;


    @Schema(description = "판매매장")
    private String storeName;

    @Schema(description = "상품번호")
    private int productNo;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "옵션")
    private String directOption;

    @Schema(description = "수량")
    private int purchaseQuantity;


    @Schema(description = "상품판매채널 번호")
    public int getChannelNo() {
        return channel.getIndex();
    }
    @Schema(description = "상품판매채널 이름")
    public String getChannelName() {
        return channel.getMeaning();
    }

    @Schema(description = "주문상태 번호")
    public int getTicketStateNo() {
        return ticketStateEnum.getStateNo();
    }

    @Schema(description = "주문상태 이름")
    public String getTicketStateName() {
        return ticketStateEnum.getMeaning();
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

    public ProductTicketSearchDto(ProductTicketEntity productTicketEntity, OrderDetailDto orderDetailDto, String productName, String storeName) {

        this.channel = orderDetailDto.getChannel();
        this.channelProductId = orderDetailDto.getChannelProductOrderId();
        this.ticketKey = productTicketEntity.getTicketKey();
        this.isUsed = productTicketEntity.isUsed();
        this.usedAt = productTicketEntity.getUsedAt();
        this.ticketStateEnum = orderDetailDto.getTicketStateEnum();
        this.cancelOrReturnStateEnum = orderDetailDto.getCancelOrReturnStateEnum();

        this.purchaseUserName = productTicketEntity.getPurchaseUserName();
        this.phoneNumber = productTicketEntity.getPhoneNumber();

        this.storeName = storeName;
        this.productNo = productTicketEntity.getProductNo();
        this.productName = productName;
        this.directOption = productTicketEntity.getDirectOption();
        this.purchaseQuantity = productTicketEntity.getPurchaseQuantity();
    }

}
