package com.kupstudio.bbarge.dto.naver.images;

import com.kupstudio.bbarge.dto.naver.product.NaverProductImageDto;
import lombok.Getter;

import java.util.List;

@Getter
public class NaverImagesResponseDto {

    private List<NaverProductImageDto> images;
}
