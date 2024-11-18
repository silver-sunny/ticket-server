package com.kupstudio.bbarge.dto.naver.product;

import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "네이버 상품 이미지 DTO")
@NoArgsConstructor
public class NaverImagesDto {

    @Schema(description = "네이버 대표 이미지 DTO")
    private NaverProductImageDto representativeImage;

    @Schema(description = "네이버 추가 이미지 DTO")
    private List<NaverProductImageDto> optionalImages;

    public NaverImagesDto(ProductRequestDto productDto) {
        this.representativeImage = new NaverProductImageDto(productDto.getPri());

        String pdi = productDto.getPdi();

        if (StringUtils.isNotEmpty(pdi)) {
            List<NaverProductImageDto> naverProductImageDtos = new ArrayList<>();
            NaverProductImageDto pdiImage = new NaverProductImageDto(pdi);
            naverProductImageDtos.add(pdiImage);
            this.optionalImages = naverProductImageDtos;

        }

    }


    public NaverImagesDto(ProductModifyRequestDto productDto) {
        this.representativeImage = new NaverProductImageDto(productDto.getPri());

        String pdi = productDto.getPdi();

        if (StringUtils.isNotEmpty(pdi)) {
            List<NaverProductImageDto> naverProductImageDtos = new ArrayList<>();
            NaverProductImageDto pdiImage = new NaverProductImageDto(pdi);
            naverProductImageDtos.add(pdiImage);
            this.optionalImages = naverProductImageDtos;

        }

    }


}
