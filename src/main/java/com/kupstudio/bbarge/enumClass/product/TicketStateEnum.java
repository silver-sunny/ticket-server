package com.kupstudio.bbarge.enumClass.product;

import lombok.Getter;

@Getter
public enum TicketStateEnum {
    ETC(-1, ""),
    NON_ISSUED(0, "발급 전"),
    AVAILABLE(1, "발급완료"),
    ALL_USED(2, "사용완료"),
    SOME_USED(3, "일부사용");


    private final int stateNo;
    private final String meaning;

    TicketStateEnum(int stateNo, String meaning) {
        this.stateNo = stateNo;
        this.meaning = meaning;
    }

    public static TicketStateEnum getEnumOfStateNo(int stateNo) {
        for (TicketStateEnum thisEnum : TicketStateEnum.values()) {
            if (thisEnum.stateNo == stateNo) return thisEnum;
        }
        return ETC;
    }
}
