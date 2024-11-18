package com.kupstudio.bbarge.dto.naver.product;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverSuccessFailProductDataDto {

    private List<String> successProductOrderIds;

    private List<NaverFailProductOrderInfosDto> failProductOrderInfos;

}
