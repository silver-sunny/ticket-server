package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum ProductOrderFilterEnum {

    ALL(0, "전체"),
    PAYMENT_WAITING(60, "결제대기"),
    CANCELED_BY_NOPAYMENT(70, "미결제취소"),
    PAYED(10, "결제완료"),
    DELIVERED(20, "배송완료"),
    RETURNED(30, "반품"),
    CANCELED(40, "취소"),
    PURCHASE_DECIDED(50, "구매확정");

    private final int index;

    private final String meaning;

    ProductOrderFilterEnum(int index, String meaning) {
        this.index = index;
        this.meaning = meaning;
    }

    public static List<Map<Object, Object>> getproductOrderFilterEnumList() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (ProductOrderFilterEnum value : ProductOrderFilterEnum.values()) {

            Map<Object, Object> channelMap = new HashMap<>();

            channelMap.put("int", value.index);
            channelMap.put("meaning", value.meaning);
            channelMap.put("enum", value);

            list.add(channelMap);
        }

        return list;
    }
}
