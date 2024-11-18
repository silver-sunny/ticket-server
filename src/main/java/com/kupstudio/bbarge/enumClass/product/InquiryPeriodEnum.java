package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum InquiryPeriodEnum {
    // Enum 변경 전에 xml 파일에서 사용 중인지 확인 후 같이 변경 하기
    ETC(0, ""),
    PURCHASE_AT(1, "결제일"),
    CANCEL_OR_RETURN_REQUEST_AT(2, "취소/반품요청일"),
    REFUND_AT(3, "환불일");

    private final int index;
    private final String meaning;

    InquiryPeriodEnum(int index, String meaning) {
        this.index = index;
        this.meaning = meaning;
    }

    public static List<Map<Object, Object>> getInquiryPeriodEnum() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (InquiryPeriodEnum value : InquiryPeriodEnum.values()) {

            if (value == InquiryPeriodEnum.ETC) {
                continue;
            }

            Map<Object, Object> channelMap = new HashMap<>();

            channelMap.put("stateNo", value.index);
            channelMap.put("meaning", value.meaning);
            channelMap.put("enum", value);

            list.add(channelMap);
        }
        return list;
    }
}
