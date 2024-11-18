package com.kupstudio.bbarge.requestDto.product;

import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductOrderRequestDto {

    @Schema(description = "구매 채널")
    @NotNull(message = "An invalid channelEnum has been entered. Only [NEVERLAND_HC, NAVER, COUPANG, WEMAKEPRICE, INTERPARK, TMON] can be entered in channelEnum")
    private ChannelEnum channelEnum;

    @Schema(description = "상품 번호")
    private int productNo;

    @Schema(description = "유저 번호")
    private int userNo;

    @Schema(description = "구매 수량")
    @Min(value = 1, message = "The minimum value of a purchaseQuantity is 1")
    private int purchaseQuantity;

    @Schema(description = "옵션 직접입력 ( 예약날짜 )", example = " ")
    private String directOption;

}
