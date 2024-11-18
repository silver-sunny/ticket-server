package com.kupstudio.bbarge.dto.naver.product;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class NaverOptionInfoDto {

    private List<NaverOptionCustomDto> optionCustom;

    public NaverOptionInfoDto() {
        this.optionCustom = Collections.singletonList(new NaverOptionCustomDto());
    }
}
