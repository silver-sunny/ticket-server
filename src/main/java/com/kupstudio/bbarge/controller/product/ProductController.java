package com.kupstudio.bbarge.controller.product;

import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import com.kupstudio.bbarge.service.product.ProductBundleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
@RequestMapping("/v1/api/product")
@Tag(name = "product-controller", description = "상품 API")
public class ProductController {

    private final ProductBundleService productBundleService;

    private final String DEFAULT_COUNT_PER_PAGE = "10";


    @ApiResponse(description = "success - 성공 | 상품 등록", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class)))
    @Operation(summary = "상품 등록 API", description = "상품을 등록합니다.<br><br>" +
            "<b>[ 필수값 ]</b><br>" +
            "storeNo: 상품을 등록 할 매장 번호<br>" +
            "name: 상품명<br>" +
            "price: 상품 가격 (100원~10,000,000원)<br>" +
            "stock: 재고 수량<br>" +
            "productStateEnum: 상품 상태 | [SELLING, WAITING_SALE] ('상품 상태 enum list' API를 확인해주세요) <br>" +
            "description: 상품 상태 | 상품 설명 <br>" +
            "priFile: 상품 대표 이미지<br><br>" +
            "<b>[ 선택값 ]</b><br>" +
            "<b>선택값을 입력하지 않으려면 null로 셋팅해주세요 (Swagger는 제거해 주세요)</b><br>" +
            "pdiFile: 상품 상세 이미지<br>" +
            "discountRate: 할인율<br>" +
            "ex) price: 50,000 discountRate: 10 일 때 상품 할인가는 10% 할인된 45000원<br>")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @PostMapping()
    public ResponseEntity<?> insertProduct(@RequestBody @Valid ProductRequestDto productRequestDto
    ) {
        return ApiResponseService.toResponseEntity(productBundleService.insertProduct(productRequestDto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 상품 정보 조회", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
            @ApiResponse(responseCode = "490", description = "저장된 상품 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "특정 상품 정보 조회 API", description = "특정 상품 정보 조회")
    @Parameter(name = "productNo", description = "조회 할 상품 번호")
    @GetMapping("/{productNo}")
    public ResponseEntity<?> getProductDetail(@PathVariable(value = "productNo") int productNo) {

        return ApiResponseService.toResponseEntity(productBundleService.getProductDetail(productNo));

    }

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 상품 수정", responseCode = "200", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "490", description = "저장된 상품 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "상품 수정 API", description = "이전에 등록한 상품의 정보를 수정합니다.<br><br>" +
            "매장명, 판매 채널은 수정 할 수 없습니다.<br>" +
            "<b>'상품 대표 이미지' 또는 '상품 상세 이미지'를 변경하려면 priFile, pdiFile 값을 셋팅하세요</b><br><br>" +
            "[ 필수값 ]<br>" +
            "productName: 상품 이름<br>" +
            "price: 상품 가격 (100원~10,000,000원)<br>" +
            "discounting: 할인 사용 여부<br>" +
            "stock: 재고 수량<br>" +
            "stateNo: 상품 상태 | '상품 상태 enum list' API를 통해 확인 가능합니다. <br>" +
            "description: 상품 상태 | 상품 설명 <br>" +
            "pri: 기존 상품 대표 이미지 | priFile 값이 있을 경우 대표 이미지 변경 <br><br>" +
            "<b>[ 선택값 ]</b><br>" +
            "<b>선택값을 입력하지 않으려면 null로 셋팅해주세요 (Swagger는 제거해 주세요)</b><br>" +
            "pdiFile: 상품 상세 이미지<br>" +
            "discountRate: 할인율<br>" +
            "ex) price: 50,000 discountRate: 10 일 때 상품 할인가는 10% 할인된 45000원<br>")
    @PutMapping(value = "")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductModifyRequestDto productModifyRequestDto
    ) {
        productBundleService.updateProduct(productModifyRequestDto);
        return ApiResponseService.toResponseEntity();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 상품 삭제", content = @Content),
            @ApiResponse(responseCode = "490", description = "저장된 상품 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "상품 삭제 API", description = "자사 상품 삭제")
    @DeleteMapping("/{productNo}")
    @Parameter(name = "productNo", description = "삭제 할 상품 번호")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productNo") int productNo) {

        productBundleService.deleteProduct(productNo);
        return ApiResponseService.toResponseEntity();

    }

    @Operation(summary = "상품 상태 enum list (등록, 수정 페이지)", description = "상품 상태 enum list<br><br>" +
            "상품 등록 또는 수정 페이지 구현 하는데 사용됩니다.")
    @GetMapping(value = "/states")
    public ResponseEntity<?> getProductStateEnumListForInsertOrUpdate() {

        return ApiResponseService.toResponseEntity(ProductStateEnum.getProductStateEnumListForInsertOrUpdate());
    }

    @Operation(summary = "상품 판매 채널 enum list", description = "상품 판매 채널 enum list")
    @GetMapping(value = "/channel")
    public ResponseEntity<?> channelEnum() {

        return ApiResponseService.toResponseEntity(ChannelEnum.getSalesChannelEnumList());
    }

    @ApiResponse(responseCode = "200", description = "success - 성공 | 전체 상품 정보 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class)))
    @Operation(summary = "전체 상품 정보 리스트 조회 API - 권한별 담당 매장", description = "상품 정보 리스트 & 페이징<br><br>" +
            "권한별 담당 매장의 등록된 상품 정보 리스트를 조회합니다.")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "페이지 번호 | 값 안 넣을 경우 1")
    @GetMapping("")
    public ResponseEntity<?> getAllStoreProductList(@RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
                                                    @RequestParam(value = "page", defaultValue = "1") int page) {

        return ApiResponseService.toResponseEntity(productBundleService.getAllStoreProductList(countPerPage, page));

    }

    @ApiResponse(description = "success - 성공 | 주문 내역 추가", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class)))
    @Operation(summary = "상품 구매 API (임시)", description = "상품 구매 API 입니다.<br><br>" +
            "입력된 구매 수량 만큼 등록된 상품의 재고 수량을 감소 시킵니다.<br>" +
            "주문 내역에 구매한 상품에 대한 정보를 추가합니다.")
    @PostMapping(value = "/purchase")
    public ResponseEntity<?> purchaseProduct(@RequestBody @Valid ProductOrderRequestDto requestDto) {
        return ApiResponseService.toResponseEntity(productBundleService.purchaseProduct(requestDto));
    }

}
