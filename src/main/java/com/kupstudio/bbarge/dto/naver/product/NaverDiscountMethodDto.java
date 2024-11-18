package com.kupstudio.bbarge.dto.naver.product;

import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "할인 혜택")
@NoArgsConstructor
public class NaverDiscountMethodDto {

    @Schema(description = "할인값", example = "10")
    private int value;

    @Schema(description = "할인 단위", example = "PERCENT")
    private String unitType;

    public NaverDiscountMethodDto(ProductRequestDto productDto) {
        this.value = productDto.getDiscountRate();
        this.unitType = "PERCENT";
    }

    public NaverDiscountMethodDto(ProductModifyRequestDto productDto) {
        this.value = productDto.getDiscountRate();
        this.unitType = "PERCENT";
    }
}
