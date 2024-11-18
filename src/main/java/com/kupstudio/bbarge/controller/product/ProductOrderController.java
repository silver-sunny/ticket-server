package com.kupstudio.bbarge.controller.product;

import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.enumClass.product.CancelOrReturnFilterEnum;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import com.kupstudio.bbarge.enumClass.product.InquiryPeriodEnum;
import com.kupstudio.bbarge.enumClass.product.ProductOrderFilterEnum;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import com.kupstudio.bbarge.service.product.order.ProductOrderBundleService;
import com.kupstudio.bbarge.service.product.order.ProductOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

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
@Tag(name = "product-order-controller", description = "주문 관리 API")
public class ProductOrderController {

    private final ProductOrderBundleService productOrderBundleService;
    private final ProductOrderService productOrderService;

    private final String DEFAULT_COUNT_PER_PAGE = "10";


    @ApiResponse(description = "success - 성공 | 주문 내역 추가", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class)))
    @Operation(summary = "주문 내역 추가 API", description = "주문 내역에 구매한 상품에 대한 정보를 추가합니다.")
    @PostMapping(value = "")
    public ResponseEntity<?> insertOrder(@RequestBody @Valid ProductOrderRequestDto requestDto) {
        return ApiResponseService.toResponseEntity(productOrderBundleService.insertOrder(requestDto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 특정 주문의 이력 정보 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailDto.class))),
            @ApiResponse(responseCode = "490", description = "저장된 상품 주문 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "주문 상세 정보 조회 API (임시)", description = "입력된 주문번호의 기본 정보를 조회합니다.<br><br>" +
            "[주문 상세 정보] 페이지 구현 할 때 사용")
    @Parameter(name = "orderNo", description = "주문번호")
    @GetMapping("/detail")
    public ResponseEntity<?> getOrderDetail(@RequestParam(value = "orderNo") int orderNo) {

        return ApiResponseService.toResponseEntity(productOrderService.getOrderDetailWithChannelDetail(orderNo));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 전체 주문 내역 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "전체 주문 내역 리스트 조회 API", description = "[전체 주문 내역] 페이지에서 사용")
    @Parameter(name = "storeNo", description = "판매매장")
    @Parameter(name = "inquiryPeriodEnum", description = "조회기간 키테고리", schema = @Schema(allowableValues = {"PURCHASE_AT", "CANCEL_OR_RETURN_REQUEST_AT", "REFUND_AT"}))
    @Parameter(name = "startDate", description = "조회 시작 날짜")
    @Parameter(name = "endDate", description = "조회 마지막 날짜")
    @Parameter(name = "productOrderFilterEnum", description = "주문상태 카테고리")
    @Parameter(name = "cancelOrReturnFilterEnum", description = "취소/반품상태 카테고리")
    @Parameter(name = "detailedConditionEnum", description = "상세조건 카테고리", schema = @Schema(allowableValues = {"PHONE_NUMBER", "PURCHASE_USER_NAME", "CHANNEL_ORDER_ID", "ORDER_NO", "PRODUCT_NO"}))
    @Parameter(name = "searchWord", description = "검색어")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "현재 위치한 페이지 (기본값 1 페이지)")
    @GetMapping("")
    public ResponseEntity<?> getAllOrderList(@RequestParam(value = "storeNo", required = false) Integer storeNo,
                                             @RequestParam(value = "inquiryPeriodEnum", required = false) InquiryPeriodEnum inquiryPeriodEnum,
                                             @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
                                             @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate,
                                             @RequestParam(value = "productOrderFilterEnum", required = false) ProductOrderFilterEnum productOrderFilterEnum,
                                             @RequestParam(value = "cancelOrReturnFilterEnum", required = false) CancelOrReturnFilterEnum cancelOrReturnFilterEnum,
                                             @RequestParam(value = "detailedConditionEnum", required = false) DetailedConditionEnum detailedConditionEnum,
                                             @RequestParam(value = "searchWord", required = false) String searchWord,
                                             @RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
                                             @RequestParam(value = "page", defaultValue = "1") int page) {

        return ApiResponseService.toResponseEntity(productOrderBundleService.getAllOrderList(storeNo, inquiryPeriodEnum, startDate, endDate, productOrderFilterEnum, cancelOrReturnFilterEnum, detailedConditionEnum, searchWord, countPerPage, page));

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 전체 주문 내역 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "미발급티켓 총 개수 조회 API", description = "[티켓 발급/취소 관리] 페이지 구현에 사용<br><br>" +
            "티켓미발급 건 (해당 부분을 구현하는데 필요한 값 입니다.)<br>" +
            "nonIssuedTicketTotalCount: 티켓미발급 건수")
    @GetMapping("/ticket/total")
    public ResponseEntity<?> getOrderNonIssuedTicketTotalCount() {

        return ApiResponseService.toResponseEntity(productOrderService.getOrderNonIssuedTicketTotalCount());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 미발급티켓 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "미발급티켓 리스트 조회 API", description = "[티켓 발급/취소 관리] 페이지 구현에 사용")
    @Parameter(name = "storeNo", description = "판매매장")
    @Parameter(name = "inquiryPeriodEnum", description = "조회기간 키테고리", schema = @Schema(allowableValues = {"PURCHASE_AT", "CANCEL_OR_RETURN_REQUEST_AT", "REFUND_AT"}))
    @Parameter(name = "startDate", description = "조회 시작 날짜")
    @Parameter(name = "endDate", description = "조회 마지막 날짜")
    @Parameter(name = "detailedConditionEnum", description = "상세조건 카테고리", schema = @Schema(allowableValues = {"PHONE_NUMBER", "PURCHASE_USER_NAME", "CHANNEL_ORDER_ID", "ORDER_NO", "PRODUCT_NO"}))
    @Parameter(name = "searchWord", description = "검색어")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "현재 위치한 페이지 (기본값 1 페이지)")
    @GetMapping("/ticket")
    public ResponseEntity<?> getOrderNonIssuedTicketList(@RequestParam(value = "storeNo", required = false) Integer storeNo,
                                                         @RequestParam(value = "inquiryPeriodEnum", required = false) InquiryPeriodEnum inquiryPeriodEnum,
                                                         @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
                                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate,
                                                         @RequestParam(value = "detailedConditionEnum", required = false) DetailedConditionEnum detailedConditionEnum,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord,
                                                         @RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
                                                         @RequestParam(value = "page", defaultValue = "1") int page) {

        return ApiResponseService.toResponseEntity(productOrderBundleService.getOrderNonIssuedTicketList(storeNo, inquiryPeriodEnum, startDate, endDate, detailedConditionEnum, searchWord, countPerPage, page));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 취소/반품 총 개수 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "취소/반품 총 개수 조회 API", description = "[취소/반품 관리] 페이지 구현에 사용<br><br>" +
            "requestTotalCount: 취소/반품 요청 건수 <br>" +
            "doneTotalCount: 취소/반품 처리 완료 건수")
    @GetMapping("/refund/total")
    public ResponseEntity<?> getOrderCancelOrReturnTotalCount() {

        return ApiResponseService.toResponseEntity(productOrderBundleService.getOrderCancelOrReturnTotalCount());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 취소/반품 총 개수 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "취소/반품 요청 리스트 조회 API", description = "[취소/반품 관리] 페이지 구현에 사용")
    @Parameter(name = "storeNo", description = "판매매장")
    @Parameter(name = "inquiryPeriodEnum", description = "조회기간 키테고리", schema = @Schema(allowableValues = {"PURCHASE_AT", "CANCEL_OR_RETURN_REQUEST_AT", "REFUND_AT"}))
    @Parameter(name = "startDate", description = "조회 시작 날짜")
    @Parameter(name = "endDate", description = "조회 마지막 날짜")
    @Parameter(name = "detailedConditionEnum", description = "상세조건 카테고리", schema = @Schema(allowableValues = {"PHONE_NUMBER", "PURCHASE_USER_NAME", "CHANNEL_ORDER_ID", "ORDER_NO", "PRODUCT_NO"}))
    @Parameter(name = "searchWord", description = "검색어")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "현재 위치한 페이지 (기본값 1 페이지)")
    @GetMapping("/refund/request")
    public ResponseEntity<?> getOrderCancelOrReturnRequestList(@RequestParam(value = "storeNo", required = false) Integer storeNo,
                                                               @RequestParam(value = "inquiryPeriodEnum", required = false) InquiryPeriodEnum inquiryPeriodEnum,
                                                               @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
                                                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate,
                                                               @RequestParam(value = "detailedConditionEnum", required = false) DetailedConditionEnum detailedConditionEnum,
                                                               @RequestParam(value = "searchWord", required = false) String searchWord,
                                                               @RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
                                                               @RequestParam(value = "page", defaultValue = "1") int page) {

        return ApiResponseService.toResponseEntity(productOrderBundleService.getOrderCancelOrReturnRequestList(storeNo, inquiryPeriodEnum, startDate, endDate, detailedConditionEnum, searchWord, countPerPage, page));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 취소/반품 처리완료 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "496", description = "담당 중인 매장이 없습니다.", content = @Content)
    })
    @Operation(summary = "취소/반품 처리완료 리스트 조회 API", description = "[취소/반품 관리] 페이지 구현에 사용")
    @Parameter(name = "storeNo", description = "판매매장")
    @Parameter(name = "inquiryPeriodEnum", description = "조회기간 키테고리", schema = @Schema(allowableValues = {"PURCHASE_AT", "CANCEL_OR_RETURN_REQUEST_AT", "REFUND_AT"}))
    @Parameter(name = "startDate", description = "조회 시작 날짜")
    @Parameter(name = "endDate", description = "조회 마지막 날짜")
    @Parameter(name = "detailedConditionEnum", description = "상세조건 카테고리", schema = @Schema(allowableValues = {"PHONE_NUMBER", "PURCHASE_USER_NAME", "CHANNEL_ORDER_ID", "ORDER_NO", "PRODUCT_NO"}))
    @Parameter(name = "searchWord", description = "검색어")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "현재 위치한 페이지 (기본값 1 페이지)")
    @GetMapping("/refund/done")
    public ResponseEntity<?> getOrderCancelOrReturnDoneList(@RequestParam(value = "storeNo", required = false) Integer storeNo,
                                                            @RequestParam(value = "inquiryPeriodEnum", required = false) InquiryPeriodEnum inquiryPeriodEnum,
                                                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
                                                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate,
                                                            @RequestParam(value = "detailedConditionEnum", required = false) DetailedConditionEnum detailedConditionEnum,
                                                            @RequestParam(value = "searchWord", required = false) String searchWord,
                                                            @RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
                                                            @RequestParam(value = "page", defaultValue = "1") int page) {

        return ApiResponseService.toResponseEntity(productOrderBundleService.getOrderCancelOrReturnDoneList(storeNo, inquiryPeriodEnum, startDate, endDate, detailedConditionEnum, searchWord, countPerPage, page));
    }


    @Operation(summary = "조회기간 enum list", description = "조회기간 enum list 입니다.<br><br>" +
            "전체 주문 내역 페이지에서 조회기간 필터값 구현하는데 사용됩니다.")
    @GetMapping(value = "/filter/period")
    public ResponseEntity<?> getInquiryPeriodEnum() {

        return ApiResponseService.toResponseEntity(InquiryPeriodEnum.getInquiryPeriodEnum());
    }

    @Operation(summary = "주문상태 enum list", description = "주문상태 enum list 입니다. (ProductOrderFilterEnum) <br><br>" +
            "전체 주문 내역 페이지에서 주문상태 필터값 구현하는데 사용됩니다.")
    @GetMapping(value = "/filter/order")
    public ResponseEntity<?> getProductOrderFilterEnumList() {

        return ApiResponseService.toResponseEntity(ProductOrderFilterEnum.getproductOrderFilterEnumList());
    }

    @Operation(summary = "취소/반품 상태 enum list", description = "취소/반품 상태 enum list 입니다. <br><br>" +
            "전체 주문 내역 페이지에서 취소/반품상태 필터값 구현하는데 사용됩니다.")
    @GetMapping(value = "/filter/refund")
    public ResponseEntity<?> getCancelOrReturnFilterEnumList() {

        return ApiResponseService.toResponseEntity(CancelOrReturnFilterEnum.getCancelOrReturnFilterEnumList());
    }

    @Operation(summary = "상세조건 enum list", description = "상세조건 enum list 입니다. <br><br>" +
            "전체 주문 내역 페이지에서 상세조건 필터값 구현하는데 사용됩니다.")
    @GetMapping(value = "/filter/detail")
    public ResponseEntity<?> getDetailedConditionEnumList() {

        return ApiResponseService.toResponseEntity(DetailedConditionEnum.getDetailedConditionEnumListForOrderListFilter());
    }

    @Operation(summary = "주문 반품 철회", description = "주문 반품 철회")
    @PutMapping("/{orderNo}/return/reject")
    public ResponseEntity<?> returnRejectOrder(@PathVariable(value = "orderNo") int orderNo,
                                               @RequestParam(value = "rejectReturnReason") String rejectReturnReason) {

        productOrderBundleService.returnRejectOrder(orderNo, rejectReturnReason);
        return ApiResponseService.toResponseEntity();

    }
}
