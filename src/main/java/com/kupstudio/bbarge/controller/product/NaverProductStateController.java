package com.kupstudio.bbarge.controller.product;


import com.kupstudio.bbarge.dao.naver.product.NaverProductStateDao;
import com.kupstudio.bbarge.enumClass.product.naver.NaverCancelReasonEnum;
import com.kupstudio.bbarge.enumClass.product.naver.NaverReturnReasonEnum;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "bad request -잘못된 형식의 구문으로 인해 서버에서 요청을 이해할 수 없습니다.", content = @Content),
        @ApiResponse(responseCode = "401", description = "unauthorized - 인증되지 않은 사용자입니다.", content = @Content),
        @ApiResponse(responseCode = "403", description = "forbidden - 권한이 없습니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found - 존재하지 않는 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed - 허용되지 않은 메소드 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "422", description = "Validation fail - 데이터 타입을 확인해 주세요.", content = @Content)})
@Tag(name = "NAVER product state", description = "네이버 상품 상태 관리 API")
@RequiredArgsConstructor
@RequestMapping("/v1/api/naver/product/order")
public class NaverProductStateController {

    private final NaverProductStateDao naverProductStateDao;


    @Operation(summary = "네이버 상품 반품 요청 승인 동시", description = "주문번호(order no)로 반품을 요청 -> 승인합니다. <br>" +
            "반품 사유<br>" +
            "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
            "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
            "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
            "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
            "    INCORRECT_INFO(\"상품 정보 상이\")<br>" +
            "    WRONG_OPTION(\"색상 등 다른 상품 잘못 배송\")")
    @Parameter(name = "orderNo", description = "반품 요청 -> 승인 할 주문번호")
    @Parameter(name = "returnReason", description = "반품 요청 -> 승인 할 주문번호")
    @PostMapping(value = "/{orderNo}/return/request-approve")
    public ResponseEntity<?> naverProductReturnRequestApprove(@PathVariable(value = "orderNo") int orderNo,
                                                              @RequestParam(value = "returnReason") NaverReturnReasonEnum returnReason) {

        naverProductStateDao.naverReturnRequestAndApprove(orderNo, returnReason);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 반품 승인", description = "주문번호(order no)로 반품을 승인합니다.")
    @Parameter(name = "orderNo", description = "반품 승인할 주문번호")
    @PostMapping(value = "/{orderNo}/return/approve")
    public ResponseEntity<?> naverProductReturnApprove(@PathVariable(value = "orderNo") int orderNo) {

        naverProductStateDao.naverReturnApprove(orderNo);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 반품 거부(철회)", description = "주문번호(order no)로 반품 거부(철회).")
    @Parameter(name = "orderNo", description = "반품 거부할 주문번호")
    @Parameter(name = "rejectReturnReason", description = "반품 거부하는 이유")
    @PostMapping(value = "/{orderNo}/return/reject")
    public ResponseEntity<?> naverProductReturnReject(@PathVariable(value = "orderNo") int orderNo,
                                                      @RequestParam(value = "rejectReturnReason") String rejectReturnReason) {

        naverProductStateDao.naverReturnReject(orderNo, rejectReturnReason);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 반품 요청", description = "테스트용 <br>" +
            "주문번호(order no)로 반품 요청<br><br>" +
            "반품 사유<br>" +
            "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
            "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
            "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
            "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
            "    INCORRECT_INFO(\"상품 정보 상이\")<br>" +
            "    WRONG_OPTION(\"색상 등 다른 상품 잘못 배송\")")
    @Parameter(name = "orderNo", description = "반품 요철할 주문번호")
    @Parameter(name = "returnReason", description = "반품 요청하는 이유")
    @PostMapping(value = "/{orderNo}/return/request")
    public ResponseEntity<?> naverProductReturnRequest(@PathVariable(value = "orderNo") int orderNo,
                                                       @RequestParam(value = "returnReason") NaverReturnReasonEnum returnReason) {

        naverProductStateDao.naverReturnRequest(orderNo, returnReason);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 취소요청 승인", description = "주문번호(order no)로 취소요청 승인")
    @Parameter(name = "orderNo", description = "취소 요청 승인할 주문번호")
    @PostMapping(value = "/{orderNo}/cancel/approve")
    public ResponseEntity<?> naverProductCancelApprove(@PathVariable(value = "orderNo") int orderNo) {

        naverProductStateDao.naverCancelApprove(orderNo);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 취소요청", description = "<br>주문번호(order no)로 취소요청<br><br>" +
            "취소 사유 <br>" +
            "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
            "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
            "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
            "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
            "    INCORRECT_INFO(\"상품 정보 상이\")")
    @Parameter(name = "orderNo", description = "취소 요청 승인할 주문번호")
    @Parameter(name = "cancelReason", description = "취소 요청 하는 이유")
    @PostMapping(value = "/{orderNo}/cancel/request")
    public ResponseEntity<?> naverProductCancelRequest(@PathVariable(value = "orderNo") int orderNo,
                                                       @RequestParam(value = "cancelReason") NaverCancelReasonEnum cancelReason) {

        naverProductStateDao.naverCancelRequest(orderNo, cancelReason);
        return ApiResponseService.toResponseEntity();

    }


    @Operation(summary = "네이버 상품 발주 / 발송 처리 ", description = "발주 / 발송 처리 ")
    @PostMapping(value = "/{orderNo}/dispatch")
    public ResponseEntity<?> naverProductDispatch(@PathVariable(value = "orderNo") int orderNo) {

        naverProductStateDao.dispatchNaverProduct(orderNo);
        return ApiResponseService.toResponseEntity();

    }


}
