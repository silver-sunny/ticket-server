package com.kupstudio.bbarge.controller.product;

import com.kupstudio.bbarge.dto.product.order.ProductOrderMemoDto;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoRequestDto;
import com.kupstudio.bbarge.service.product.order.ProductOrderMemoBundleService;
import com.kupstudio.bbarge.service.product.order.ProductOrderMemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "bad request - 잘못된 형식의 구문으로 인해 서벋에서 요청을 이해할 수 없습니다.", content = @Content),
        @ApiResponse(responseCode = "401", description = "unauthorized - 인증되지 않은 사용자입니다.", content = @Content),
        @ApiResponse(responseCode = "403", description = "forbidden - 권한이 없습니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found - 존재하지 않는 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed - 허용되지 않은 메소드 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "422", description = "Validation fail - 데이터 타입을 확인해 주세요.", content = @Content)})
@RequiredArgsConstructor
@RequestMapping("/v1/api/product/order")
@Tag(name = "product-order-memo-controller", description = "주문 메모 API")
public class ProductOrderMemoController {

    private final ProductOrderMemoBundleService productOrderMemoBundleService;
    private final ProductOrderMemoService productOrderMemoService;

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 메모 추가", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class))),
            @ApiResponse(responseCode = "490", description = "저장된 상품 주문 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "메모 추가 API", description = "입력한 주문번호에 메모 추가<br><br>" +
            "[ 필수값 ]<br>" +
            "orderNo: 메모를 입력할 주문번호<br>" +
            "memo: 메모 내용")
    @PostMapping(value = "/{orderNo}/memo")
    public ResponseEntity<?> insertMemo(@PathVariable(value = "orderNo") int orderNo,
                                        @RequestBody @Valid ProductOrderMemoRequestDto requestDto) {
        return ApiResponseService.toResponseEntity(productOrderMemoBundleService.insertMemo(orderNo, requestDto));
    }

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 메모 리스트 조회", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductOrderMemoDto.class)))),
            @ApiResponse(responseCode = "490", description = "저장된 상품 주문 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "특정 주문의 메모 리스트 조회 API", description = "입력한 주문번호의 메모 리스트 조회")
    @Parameter(name = "orderNo", description = "주문번호")
    @GetMapping(value = "/{orderNo}/memo")
    public ResponseEntity<?> getMemoList(@PathVariable(value = "orderNo") int orderNo) {
        return ApiResponseService.toResponseEntity(productOrderMemoBundleService.getMemoList(orderNo));
    }

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 메모 삭제", responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "490", description = "저장된 메모가 없습니다.", content = @Content)
    })
    @Operation(summary = "메모 조회 API", description = "입력된 주문번호의 특정 메모의 정보를 조회합니다.")
    @Parameter(name = "orderNo", description = "조회할 메모가 작성된 주문의 주문번호")
    @Parameter(name = "memoNo", description = "조회할 메모번호")
    @GetMapping(value = "/{orderNo}/memo/{memoNo}")
    public ResponseEntity<?> getMemoDetail(@PathVariable(value = "orderNo") int orderNo,
                                           @PathVariable(value = "memoNo") int memoNo) {

        return ApiResponseService.toResponseEntity(productOrderMemoService.getMemoDetail(orderNo, memoNo));
    }

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 메모 삭제", responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "482", description = "작성자가 일치하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "490", description = "저장된 메모가 없습니다.", content = @Content)
    })
    @Operation(summary = "특정 주문의 특정 메모 수정 API", description = "이전에 등록한 특정 주문의 메모를 수정합니다.<br><br>" +
            "[ 필수값 ]<br>" +
            "orderNo: 메모를 입력할 주문번호<br>" +
            "memo: 메모 내용")
    @Parameter(name = "orderNo", description = "삭제할 메모가 작성된 주문의 주문번호")
    @PutMapping(value = "/{orderNo}/memo")
    public ResponseEntity<?> updateMemo(@PathVariable(value = "orderNo") int orderNo,
                                        @RequestBody @Valid ProductOrderMemoModifyRequestDto requestDto) {

        productOrderMemoBundleService.updateMemo(orderNo, requestDto);
        return ApiResponseService.toResponseEntity();
    }

    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 메모 삭제", responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "482", description = "작성자가 일치하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "490", description = "저장된 메모가 없습니다.", content = @Content)
    })
    @Operation(summary = "특정 주문의 특정 메모 삭제 API", description = "입력된 주문번호의 특정 메모를 삭제합니다.")
    @Parameter(name = "orderNo", description = "삭제할 메모가 작성된 주문의 주문번호")
    @Parameter(name = "memoNo", description = "삭제할 메모번호")
    @DeleteMapping(value = "/{orderNo}/memo/{memoNo}")
    public ResponseEntity<?> deleteMemo(@PathVariable(value = "orderNo") int orderNo,
                                        @PathVariable(value = "memoNo") int memoNo) {

        productOrderMemoBundleService.deleteMemo(orderNo, memoNo);
        return ApiResponseService.toResponseEntity();
    }
}
