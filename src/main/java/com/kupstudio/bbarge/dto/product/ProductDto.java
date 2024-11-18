package com.kupstudio.bbarge.dto.product;

import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {

    @Schema(description = "상품이 등록된 매장 번호")
    private int storeNo;

    @Schema(description = "상품 매장 이름")
    private String storeName;

    @Schema(description = "상품 생성 번호")
    private int productNo;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "할인가")
    private Integer discountPrice;

    @Schema(description = "할인율")
    private Integer discountRate;

    @Schema(description = "재고 수량")
    private int stock;


    @Schema(description = "상품 상태 enum")
    private ProductStateEnum productStateEnum;

    @Schema(description = "상품 판매 채널")
    private ChannelEnum channel;

    @Schema(description = "상품 판매 채널 상세정보")
    private ChannelDetailDto channelDetail;

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

    @Schema(description = "채널 상품 아이디")
    private String channelProductId;

    @Schema(description = "상품 등록자")
    private String registrant;

    @Schema(description = "삭제 여부")
    private boolean isDelete;

    public ProductDto() {
        this.storeNo = 0;
        this.storeName = "";
        this.productNo = 0;
        this.productName = "";
        this.price = 0;
        this.discountPrice = 0;
        this.stock = 0;
        this.productStateEnum = ProductStateEnum.ETC;
        this.channel = ChannelEnum.ETC;
        this.pri = "";
        this.pdi = "";
        this.description = "";
        this.createAt = null;
        this.modifyAt = null;
        this.channelProductId = "";
        this.registrant = "";
        this.isDelete = false;
    }

    @Schema(description = "상품 상태 번호")
    public int getProductStateNo() {
        return productStateEnum.stateNo;
    }

    @Schema(description = "상품 상태 이름")
    public String getProductStateName() {
        return productStateEnum.meaning;
    }

    public void setStateNo(int typeNo) {
        productStateEnum = ProductStateEnum.getEnumOfStateNo(typeNo);
    }

    public void setChannelNo(int index) {
        channel = ChannelEnum.getEnumOfIndex(index);
    }

    public void setChannelDetail(ChannelDetailDto channelDetail) {
        this.channelDetail = channelDetail;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
        calculateDiscountPrice();
    }

    private void calculateDiscountPrice() {
        if (discountRate != null) {
            double discountMultiplier = 1 - (discountRate / 100.0);
            double discountedPrice = price * discountMultiplier;
            this.discountPrice = (int) Math.round(discountedPrice);
        }
    }
}
