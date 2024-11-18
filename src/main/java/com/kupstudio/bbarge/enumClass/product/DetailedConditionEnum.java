package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum DetailedConditionEnum {
    // Enum 변경 전에 xml 파일에서 사용 중인지 확인 후 같이 변경 하기
    ETC(0, ""),
    PHONE_NUMBER(1, "구매자연락처"),
    PURCHASE_USER_NAME(2, "구매자명"),
    CHANNEL_ORDER_ID(3, "소셜주문번호"),
    ORDER_NO(4, "주문번호"),
    PRODUCT_NO(5, "상품번호"),
    TICKET_KEY(6, "티켓번호 (예약번호)");


    private final int index;
    private final String meaning;

    DetailedConditionEnum(int index, String meaning) {
        this.index = index;
        this.meaning = meaning;
    }

    // 주문 관리 -> 전체 주문 내역 - 상세조건 필터
    public static List<Map<Object, Object>> getDetailedConditionEnumListForOrderListFilter() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (DetailedConditionEnum value : DetailedConditionEnum.values()) {
            if (value == DetailedConditionEnum.ETC || value == DetailedConditionEnum.TICKET_KEY) {
                continue;
            }

            Map<Object, Object> enumMap = new HashMap<>();

            enumMap.put("index", value.index);
            enumMap.put("meaning", value.meaning);
            enumMap.put("enum", value);
            list.add(enumMap);
        }
        return list;
    }

    // 티켓 사용처리 -> 티켓사용처리 - 검색 필터
    public static List<Map<Object, Object>> getDetailedConditionEnumListForTicketListFilter() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (DetailedConditionEnum value : DetailedConditionEnum.values()) {
            if (value == DetailedConditionEnum.PHONE_NUMBER ||
                value == DetailedConditionEnum.CHANNEL_ORDER_ID ||
                value == DetailedConditionEnum.TICKET_KEY ||
                value == DetailedConditionEnum.ORDER_NO) {

                Map<Object, Object> enumMap = new HashMap<>();

                enumMap.put("index", value.index);
                enumMap.put("meaning", value.meaning);
                enumMap.put("enum", value);

                list.add(enumMap);
            }
        }
        return list;
    }
}
