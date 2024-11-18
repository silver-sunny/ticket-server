package com.kupstudio.bbarge.controller.product;


import com.kupstudio.bbarge.dao.naver.product.NaverProductDao;
import com.kupstudio.bbarge.dto.naver.images.NaverImagesResponseDto;
import com.kupstudio.bbarge.dto.naver.product.NaverProductDto;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import com.kupstudio.bbarge.service.naver.product.NaverProductBundleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "bad request -잘못된 형식의 구문으로 인해 서버에서 요청을 이해할 수 없습니다.", content = @Content),
        @ApiResponse(responseCode = "401", description = "unauthorized - 인증되지 않은 사용자입니다.", content = @Content),
        @ApiResponse(responseCode = "403", description = "forbidden - 권한이 없습니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found - 존재하지 않는 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed - 허용되지 않은 메소드 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "422", description = "Validation fail - 데이터 타입을 확인해 주세요.", content = @Content)})
@RequiredArgsConstructor
@Tag(name = "NAVER-product-controller", description = "네이버 상품 API")
@RequestMapping("/v1/api/naver")
public class NaverProductController {

    private final NaverProductDao naverProductDao;

    private final NaverProductBundleService naverProductBundleService;


    @Operation(summary = "네이버 상품 등록 API", description = "")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @PostMapping(value = "/product")
    public ResponseEntity<?> insertNaverProduct(@RequestBody @Valid ProductRequestDto productRequest) {

        naverProductBundleService.insertOrUpdateNaverProductOfState(productRequest);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 수정 API", description = "")
    @PutMapping(value = "/product")
    public ResponseEntity<?> insertNaverProduct(@RequestBody @Valid ProductModifyRequestDto productRequest) {

        naverProductBundleService.updateAfterInsertNaverProduct(productRequest);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 이미지 등록", description = "")
    @ApiResponse(description = "success - 성공 | 이미지 등록", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NaverImagesResponseDto.class)))
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadNaverProductImages(@RequestPart("file") List<MultipartFile> files) {


        return ApiResponseService.toResponseEntity(naverProductDao.uploadNaverProductImages(files));
    }


    @ApiResponse(description = "success - 성공 | 네이버 상품 정보", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NaverProductDto.class)))
    @Operation(summary = "네이버 상품 하나 가져오기", description = "테스트용")
    @GetMapping(value = "/{channelProductNo}")
    public ResponseEntity<?> getNaverProduct(@PathVariable(value = "channelProductNo") String channelProductNo) {

        return ApiResponseService.toResponseEntity(naverProductDao.getNaverProduct(channelProductNo));
    }


    @Operation(summary = "네이버 상품 삭제", description = "")
    @DeleteMapping(value = "/product/{productNo}")
    public ResponseEntity<?> deleteNaverProduct(@PathVariable(value = "productNo") int productNo) {

        naverProductBundleService.deleteNaverProduct(productNo);
        return ApiResponseService.toResponseEntity();
    }

}
