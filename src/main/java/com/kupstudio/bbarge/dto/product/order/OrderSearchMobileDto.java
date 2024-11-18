package com.kupstudio.bbarge.dto.product.order;

import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchMobileDto {
    @Schema(description = "소셜주문번호")
    private String channelOrderId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "구매자명")
    private String purchaseUserName;

    @Schema(description = "구매자연락처")
    private String phoneNumber;

    public OrderSearchMobileDto(ProductOrderEntity productOrderEntity){
        this.channelOrderId = productOrderEntity.getChannelOrderId();
        this.purchaseUserName = productOrderEntity.getPurchaseUserName();
        this.phoneNumber = productOrderEntity.getPhoneNumber();
    }
}
