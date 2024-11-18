package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum CancelOrReturnFilterEnum {
    ALL(0, "전체"),
    CANCEL_REQUEST(10, "취소요청"),
    CANCEL_DONE(12, "취소완료"),
    CANCEL_REJECT(11, "취소철회"),
    RETURN_REQUEST(20, "반품요청"),
    RETURN_DONE(22, "반품완료"),
    RETURN_REJECT(21, "반품철회");


    private final int statusNo;
    private final String meaning;

    CancelOrReturnFilterEnum(int statusNo, String meaning) {
        this.statusNo = statusNo;
        this.meaning = meaning;
    }

    public static List<Map<Object, Object>> getCancelOrReturnFilterEnumList() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (CancelOrReturnFilterEnum value : CancelOrReturnFilterEnum.values()) {

            Map<Object, Object> channelMap = new HashMap<>();

            channelMap.put("index", value.statusNo);
            channelMap.put("meaning", value.meaning);
            channelMap.put("enum", value);

            list.add(channelMap);
        }
        return list;
    }
}
