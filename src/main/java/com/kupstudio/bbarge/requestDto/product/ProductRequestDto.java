package com.kupstudio.bbarge.requestDto.product;

import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.validation.validInterface.product.ProductPriceValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProductRequestDto {

    @Schema(description = "매장 번호")
    @Min(value = 1, message = "The minimum value of a productNo is 1")
    private int storeNo;

    @Schema(description = "상품명", example = "상품명")
    @Size(max = 100, message = "productName max is 100")
    @NotBlank(message = "productName is blank, insert productName")
    private String productName;

    @Schema(description = "상품 가격", example = "500,000")
    @Min(value = 1000, message = "The minimum value of a price is 1000")
    @Max(value = 10000000, message = "The maximum value of price is 10,000,000")
    @ProductPriceValid
    private int price;

    @Schema(description = "할인율", example = "10")
    @Min(value = 1, message = "The minimum value of a discountRate is 1")
    @Max(value = 99, message = "The maximum value of discountRate is 99")
    private Integer discountRate;

    @Schema(description = "재고 수량")
    @Min(value = 1, message = "The minimum value of a stock is 1")
    private int stock;

    @Schema(description = "상품 상태", example = "SALE")
    @NotNull(message = "An invalid productStateEnum has been entered. Only [SALE, SUSPENSION] can be entered in productStateEnum")
    private ProductStateEnum productStateEnum;

    @Schema(description = "상품 대표 이미지", example = "http://shop1.phinf.naver.net/20231215_96/17026221546862zJQh_PNG/15897328479152218_781934187.png")
    @NotBlank(message = "pri is blank, insert pri")
    private String pri;

    @Schema(description = "상품 상세 이미지", example = "http://shop1.phinf.naver.net/20231215_96/17026221546862zJQh_PNG/15897328479152218_781934187.png")
    private String pdi;

    @Schema(description = "상품 설명", example = "상품 설명")
    @Size(max = 3000, message = "contents max is 3000")
    @NotBlank(message = "description is blank, insert description")
    private String description;
}
