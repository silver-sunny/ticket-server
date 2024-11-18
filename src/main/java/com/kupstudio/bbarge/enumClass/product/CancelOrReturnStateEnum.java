package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

@Getter
public enum CancelOrReturnStateEnum {
    ETC(0, "-", true),
    // 자체 액션 가능
    CANCEL_REQUEST(10, "취소요청", false),
    CANCEL_REJECT(11, "취소철회", true),
    CANCEL_DONE(12, "취소처리완료", false),
    CANCELING(13, "취소처리중", false),

    RETURN_REQUEST(20, "반품요청", false),
    RETURN_REJECT(21, "반품철회", true),
    RETURN_DONE(22, "반품완료", false),

    PURCHASE_DECISION_REQUEST(30, "확정요청", true),
    PURCHASE_DECISION_HOLDBACK_RELEASE(31, "확정보류해제", true),
    PURCHASE_DECISION_HOLDBACK(32, "확정보류", true),

    ADMIN_CANCEL_REJECT(40, "직권취소철회", true),

    // 자체액션 안됨
    ADMIN_CANCEL_DONE(41, "직권취소완료", false),
    ADMIN_CANCELING(42, "직권취소중", false),

    COLLECTING(50, "수거처리중", false),
    COLLECT_DONE(51, "수거완료", false),

    EXCHANGE_REQUEST(60, "교환요청", false),
    EXCHANGE_REJECT(61, "교환철회", false),
    EXCHANGE_DONE(62, "교환완료", false),
    EXCHANGE_REDELIVERING(63, "교환재배송중", false);


    private final int statusNo;
    private final String meaning;
    private final boolean usable;

    CancelOrReturnStateEnum(int statusNo, String meaning, boolean usable) {
        this.statusNo = statusNo;
        this.meaning = meaning;
        this.usable = usable;
    }

    public static CancelOrReturnStateEnum getEnumOfStatusNo(int statusNo) {
        for (CancelOrReturnStateEnum thisEnum : CancelOrReturnStateEnum.values()) {
            if (thisEnum.statusNo == statusNo) return thisEnum;
        }
        return ETC;
    }
}
