package com.kupstudio.bbarge.dto.naver.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "원산지 정보 DTO")
public class NaverOriginAreaInfoDto {

    @Schema(description = "원산지 상세 지역 코드", example = "00", defaultValue = "00")
    private String originAreaCode;

    public NaverOriginAreaInfoDto(){
        this.originAreaCode = "00";
    }

}
