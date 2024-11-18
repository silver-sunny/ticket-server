package com.kupstudio.bbarge.dto.product;

import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnStateEnum;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductTicketListDto {

    @Schema(description = "티켓번호 (react key 값)")
    private int ticketNo;

    @Schema(description = "주문번호")
    private int orderNo;

    @Schema(description = "구매채널")
    private ChannelEnum channel;

    @Schema(description = "구매채널 상세정보")
    private ChannelDetailDto channelDetailDto;

    @Schema(description = "소셜주문번호")
    private String channelOrderId;

    @Schema(description = "구매자명")
    private String purchaseUserName;

    @Schema(description = "구매자연락처")
    private String phoneNumber;

    @Schema(description = "티켓 키 (티켓번호)")
    private String ticketKey;

    @Schema(description = "티켓상태")
    private boolean isUsed;

    @Schema(description = "티켓사용일시")
    private LocalDateTime usedAt;

    @Schema(description = "취소/반품상태")
    private CancelOrReturnStateEnum cancelOrReturnStateEnum;

    @Schema(description = "판매매장")
    private String storeName;

    @Schema(description = "상품번호")
    private int productNo;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Schema(description = "처리중 여부")
    private boolean isProgressing;

    @Schema(description = "티켓 사용 가능 여부")
    private boolean usable;

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

    @Schema(description = "취소/반품상태에 따른 사용가능 여부")
    public Boolean getCancelOrReturnUsable() {
        if (cancelOrReturnStateEnum != null) {
            return cancelOrReturnStateEnum.isUsable();
        }
        return null;
    }

    public ProductTicketListDto(ProductTicketEntity entity, ProductDto productDto, OrderDetailDto orderDetailDto, String storeName) {
        this.ticketNo = entity.getTicketNo();
        this.orderNo = entity.getOrderNo();
        this.channel = productDto.getChannel();
        this.channelDetailDto = ChannelEnum.getChannelDetail(productDto);;
        this.channelOrderId = productDto.getChannelProductId();
        this.purchaseUserName = entity.getPurchaseUserName();
        this.phoneNumber = entity.getPhoneNumber();
        this.ticketKey = entity.getTicketKey();
        this.isUsed = entity.isUsed();
        this.usedAt = entity.getUsedAt();
        this.cancelOrReturnStateEnum = orderDetailDto.getCancelOrReturnStateEnum();
        this.storeName = storeName;
        this.productNo = entity.getProductNo();
        this.productName = productDto.getProductName();
        this.directOption = entity.getDirectOption();
        this.isProgressing = orderDetailDto.isProgressing();
        this.usable = !entity.isUsed() && !orderDetailDto.isProgressing() && orderDetailDto.getCancelOrReturnStateEnum().isUsable();
    }
}
