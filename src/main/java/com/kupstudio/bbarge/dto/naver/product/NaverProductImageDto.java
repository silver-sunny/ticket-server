package com.kupstudio.bbarge.dto.naver.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "대표 이미지 DTO")
@NoArgsConstructor
public class NaverProductImageDto {

    @Schema(description = "이미지 url 대표이미지는 필수, 여러장은 옵션", example = "http://shop1.phinf.naver.net/20231206_138/1701835299704au3YK_PNG/16163404581303779_2123662756.png")
    private String url;

    public NaverProductImageDto(String url) {
        this.url = url;
    }
}
