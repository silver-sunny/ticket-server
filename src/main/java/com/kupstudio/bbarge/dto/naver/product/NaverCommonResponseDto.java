package com.kupstudio.bbarge.dto.naver.product;

import lombok.Getter;

@Getter
public class NaverCommonResponseDto {

    private String timestamp;

    private NaverSuccessFailProductDataDto data;

    private String code;

    private String traceId;

    private String message;
}
