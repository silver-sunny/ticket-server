package com.kupstudio.bbarge.enumClass.product.naver;

import lombok.Getter;

@Getter
public enum NaverReturnReasonEnum {

    INTENT_CHANGED("구매 의사 취소"),
    COLOR_AND_SIZE("색상 및 사이즈 변경"),
    WRONG_ORDER("다른 상품 잘못 주문"),
    PRODUCT_UNSATISFIED("서비스 불만족"),
    INCORRECT_INFO("상품 정보 상이"),
    WRONG_OPTION("색상 등 다른 상품 잘못 배송");

    private final String meaning;

    NaverReturnReasonEnum(String meaning) {
        this.meaning = meaning;
    }

}
