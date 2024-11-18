package com.kupstudio.bbarge.dto.naver.product;

import lombok.Getter;

@Getter
public class NaverOptionCustomDto {

    private String groupName;

    public NaverOptionCustomDto() {
        this.groupName = "예약날짜";
    }

}
