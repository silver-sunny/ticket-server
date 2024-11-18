package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

@Getter
public enum ProductOrderStatusTypeEnum {

    ETC(0, ""),

    // 자체 사용
    PAYED(10, "결제 완료"),
    DELIVERED(20, "배송 완료"),
    RETURNED(30, "반품"),
    CANCELED(40, "취소"),
    PURCHASE_DECIDED(50, "구매확정"),
    PAYMENT_WAITING(60, "결제대기"),
    CANCELED_BY_NOPAYMENT(70, "미결제취소"),

    // 자체에서 안사용하지만 네이버쪽에 있는 status
    EXCHANGED(80, "교환"),
    DELIVERING(90, "배송 중");

    private final int index;
    private final String meaning;

    ProductOrderStatusTypeEnum(int index, String meaning) {
        this.index = index;
        this.meaning = meaning;
    }

    public static ProductOrderStatusTypeEnum getEnumOfIndex(int index) {
        for (ProductOrderStatusTypeEnum thisEnum : ProductOrderStatusTypeEnum.values()) {
            if (thisEnum.index == index) return thisEnum;
        }

        return ETC;
    }
}
