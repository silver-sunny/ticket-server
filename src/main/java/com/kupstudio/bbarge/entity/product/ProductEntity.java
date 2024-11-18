package com.kupstudio.bbarge.entity.product;

import com.kupstudio.bbarge.dto.naver.product.NaverCustomerBenefitDto;
import com.kupstudio.bbarge.dto.naver.product.NaverImagesDto;
import com.kupstudio.bbarge.dto.naver.product.NaverOriginProductDto;
import com.kupstudio.bbarge.dto.naver.product.NaverProductDto;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductEntity {
    @Schema(description = "상품을 등록할 매장 번호")
    private Integer storeNo;

    @Schema(description = "상품 생성 번호")
    private Integer productNo;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "할인율")
    private Integer discountRate;

    @Schema(description = "재고 수량")
    private int stock;

    @Schema(description = "상품 상태 번호")
    private int stateNo;


    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private String pri;

    @Schema(description = "상품 상세 이미지 (Product Detail Image)")
    private String pdi;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "매장 정보 등록 시간")
    private LocalDateTime createAt;

    @Schema(description = "매장 정보 수정 시간")
    private LocalDateTime modifyAt;

    @Schema(description = "채널 번호")
    private int channelNo;

    @Schema(description = "채널 상품 아이디")
    private String channelProductId;

    @Schema(description = "상품 등록자")
    private String registrant;

    @Schema(description = "삭제 여부")
    private boolean isDelete;

    public ProductEntity ProductRequestDtoToProductEntity(ProductRequestDto requestDto) {

        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        return ProductEntity.builder()
                .storeNo(requestDto.getStoreNo())
                .productName(requestDto.getProductName())
                .price(requestDto.getPrice())
                .discountRate(requestDto.getDiscountRate())
                .stock(requestDto.getStock())
                .stateNo(requestDto.getProductStateEnum().getStateNo())
                .pri(requestDto.getPri())
                .pdi(requestDto.getPdi())
                .description(requestDto.getDescription())
                .createAt(currentDate)
                .build();
    }

    public ProductEntity ProductModifyRequestDtoToProductEntity(ProductModifyRequestDto requestDto) {
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        return ProductEntity.builder()
                .productNo(requestDto.getProductNo())
                .productName(requestDto.getProductName())
                .price(requestDto.getPrice())
                .discountRate(requestDto.getDiscountRate())
                .stock(requestDto.getStock())
                .stateNo(requestDto.getProductStateEnum().getStateNo())
                .pri(requestDto.getPri())
                .pdi(requestDto.getPdi())
                .description(requestDto.getDescription())
                .modifyAt(currentDate)
                .build();
    }


    public ProductEntity NaverProductToProductEntity(Integer storeNo, Integer productNo, String channelProductId, NaverProductDto requestDto, LocalDateTime createAt, LocalDateTime modifyAt) {

        NaverOriginProductDto naverOriginProduct = requestDto.getOriginProduct();
        NaverImagesDto naverImagesDto = naverOriginProduct.getImages();


        String pdi = !naverImagesDto.getOptionalImages().isEmpty() ? naverImagesDto.getOptionalImages().get(0).getUrl() : null;
        return ProductEntity.builder()
                .productNo(productNo)
                .storeNo(storeNo)
                .productName(naverOriginProduct.getName())
                .price(naverOriginProduct.getSalePrice())
                .discountRate(getValueFromDiscountPolicy(naverOriginProduct.getCustomerBenefit()))
                .stock(naverOriginProduct.getStockQuantity())
                .stateNo(ProductStateEnum.getProductStateByNaverProductStatusEnum(naverOriginProduct.getStatusType()).getStateNo())
                .pri(naverImagesDto.getRepresentativeImage().getUrl())
                .pdi(pdi)
                .description(naverOriginProduct.getDetailContent())
                .createAt(createAt)
                .modifyAt(modifyAt)
                .channelNo(ChannelEnum.NAVER.getIndex())
                .channelProductId(channelProductId)
                .build();
    }

    /**
     * 할인율
     * 네이버 api update후 변경 예정
     *
     * @param naverOriginProduct
     * @return
     */
    private Integer getValueFromDiscountPolicy(NaverCustomerBenefitDto naverOriginProduct) {
        if (naverOriginProduct.getImmediateDiscountPolicy() != null
                && naverOriginProduct.getImmediateDiscountPolicy().getDiscountMethod() != null) {
            return naverOriginProduct.getImmediateDiscountPolicy().getDiscountMethod().getValue();
        }
        return null;
    }
}
