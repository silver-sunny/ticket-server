package com.kupstudio.bbarge.entity.product;

import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.user.UserInfoDto;
import com.kupstudio.bbarge.enumClass.product.ProductOrderStatusTypeEnum;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductOrderEntity {

    @Schema(description = "주문번호")
    private int orderNo;

    @Schema(description = "구매채널")
    private int channelNo;

    @Schema(description = "소셜주문번호")
    private String channelOrderId;

    @Schema(description = "주문상태 번호")
    private int orderStateNo;

    @Schema(description = "반품 or 취소 상태 번호")
    private Integer cancelOrReturnStateNo;

    @Schema(description = "매장 번호")
    private int storeNo;

    @Schema(description = "상품 번호")
    private int productNo;

    @Schema(description = "구매일")
    private LocalDateTime purchaseAt;

    @Schema(description = "구매금액")
    private long purchasePrice;

    @Schema(description = "구매수량")
    private int purchaseQuantity;

    @Schema(description = "구매자 아이디")
    private String purchaseUserId;

    @Schema(description = "구매자 이름")
    private String purchaseUserName;

    @Schema(description = "구매자 연락처")
    private String phoneNumber;

    @Schema(description = "취소 요청일")
    private String cancelRequestAt;

    @Schema(description = "취소 철회일")
    private String cancelRejectAt;

    @Schema(description = "취소 완료일")
    private String cancelDoneAt;

    @Schema(description = "반품 요청일")
    private String returnRequestAt;

    @Schema(description = "반품 철회일")
    private String returnRejectAt;

    @Schema(description = "반품 완료일")
    private String returnDoneAt;

    @Schema(description = "구매 내역 등록 시간")
    private LocalDateTime createAt;

    @Schema(description = "구매 내역 수정 시간")
    private LocalDateTime modifyAt;

    @Schema(description = "옵션(소셜상품)주문번호")
    private String channelProductOrderId;

    @Schema(description = "취소/반품 사유")
    private String cancelOrReturnReason;

    @Schema(description = "취소/반품 상세 사유")
    private String cancelOrReturnDetailReason;

    @Schema(description = "취소/반품 요청자")
    private String cancelOrReturnRequester;

    @Schema(description = "통신 대기 여부")
    private Boolean isProgressing;

    @Schema(description = "옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Schema(description = "티켓 상태 번호")
    private int ticketStateNo;

    public static ProductOrderEntity ProductOrderRequestDtToProductOrderEntity(String directOption, ProductOrderRequestDto requestDto, ProductDto productDto, UserInfoDto userInfoDto) {

        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        int price = productDto.getPrice();
        Integer discountRate = productDto.getDiscountRate();
        long purchasePrice;

        if (discountRate != null) {
            double discountMultiplier = 1 - (discountRate / 100.0);
            double discountedPrice = price * discountMultiplier;
            purchasePrice = Math.round(discountedPrice * requestDto.getPurchaseQuantity());
        } else {
            purchasePrice = (long) price * requestDto.getPurchaseQuantity();
        }

        return ProductOrderEntity.builder()
                .channelNo(requestDto.getChannelEnum().getIndex())
                .orderStateNo(ProductOrderStatusTypeEnum.PAYED.getIndex())
                .storeNo(productDto.getStoreNo())
                .productNo(requestDto.getProductNo())
                .purchaseAt(currentDate)
                .purchasePrice(purchasePrice)
                .purchaseQuantity(requestDto.getPurchaseQuantity())
                .purchaseUserId(userInfoDto.getUserId())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .createAt(currentDate)
                .purchaseUserName(userInfoDto.getName())
                .directOption(directOption)
                .build();
    }
}
