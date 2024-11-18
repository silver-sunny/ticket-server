package com.kupstudio.bbarge.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupstudio.bbarge.dto.product.ProductTicketInfoDto;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.service.product.ProductTicketBundleService;
import com.kupstudio.bbarge.service.product.ProductTicketService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
@RequestMapping("/v1/api/product/ticket")
@Tag(name = "product ticket Controller", description = "상품 티켓 API")
public class ProductTicketController {

    private final ProductTicketService productTicketService;

    private final ProductTicketBundleService productTicketBundleService;

    private final String DEFAULT_COUNT_PER_PAGE = "10";


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 티켓 발급", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StoreDto.class)))),
            @ApiResponse(responseCode = "495", description = "문자를 전송할 휴대전화 번호가 알맞지 않아 전송에 실패하였습니다.", content = @Content)
    })
    @Operation(summary = "티켓 발급", description = "티켓 발급")
    @Parameter(name = "orderNo", description = "구매 수량 10000개 이하만 호출 가능")
    @PostMapping("/{orderNo}")
    public ResponseEntity<?> insertProductTicket(@PathVariable(value = "orderNo") @NotNull int orderNo) throws NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {

        return ApiResponseService.toResponseEntity(productTicketBundleService.insertProductTicket(orderNo));

    }


    @Operation(summary = "티켓 사용 처리", description = "티켓 사용 처리")
    @PutMapping("/used")
    public ResponseEntity<?> updateProductTicketUsed(@RequestBody List<String> ticketKeys) {

        productTicketBundleService.updateProductTicketUsed(ticketKeys);
        return ApiResponseService.toResponseEntity();

    }


    @ApiResponse(responseCode = "200", description = "success - 상품 정보 조회", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductTicketInfoDto.class))))
    @Operation(summary = "티켓 조회 (티켓 안내 문자 url)", description = "티켓 키로 조회")
    @Parameter(name = "ticketKey", description = "조회할 티켓 키")
    @GetMapping("")
    public ResponseEntity<?> getProductTicketInfo(@RequestParam(value = "ticketKey") String ticketKey) {


        return ApiResponseService.toResponseEntity(productTicketBundleService.getProductTicketInfo(ticketKey));

    }

    @Operation(summary = "티켓 검색 ([티켓 사용처리 페이지] 티켓번호 검색 기능)", description = "티켓 검색")
    @Parameter(name = "ticketKey", description = "조회할 티켓 키")
    @GetMapping("/search")
    public ResponseEntity<?> getProductTicketSearch(@RequestParam(value = "ticketKey") String ticketKey) {
        return ApiResponseService.toResponseEntity(productTicketBundleService.getProductTicketSearch(ticketKey));
    }

    @Operation(summary = "검색 필터 enum list", description = "검색 필터 enum list 입니다. <br><br>" +
            "티켓 사용처리 페이지에서 검색 필터 구현하는데 사용됩니다.")
    @GetMapping(value = "/filter")
    public ResponseEntity<?> getDetailedConditionEnumList() {

        return ApiResponseService.toResponseEntity(DetailedConditionEnum.getDetailedConditionEnumListForTicketListFilter());
    }

    @Operation(summary = "티켓 리스트 (PC 티켓 사용처리 페이지)", description = "PC 티켓 사용처리 페이지 구현을 위해 사용됩니다.")
    @Parameter(name = "detailedConditionEnum", description = "검색 조건", schema = @Schema(allowableValues = {"PHONE_NUMBER", "CHANNEL_ORDER_ID", "TICKET_KEY", "ORDER_NO"}))
    @Parameter(name = "searchWord", description = "검색어")
    @Parameter(name = "countPerPage", description = "한 페이지에서 데이터 가져올 갯수 | 값 안 넣을 경우 10")
    @Parameter(name = "page", description = "현재 위치한 페이지 (기본값 1 페이지)")
    @GetMapping("list")
    public ResponseEntity<?> getProductTicketList(
            @RequestParam(value = "detailedConditionEnum") DetailedConditionEnum detailedConditionEnum,
            @RequestParam(value = "searchWord") String searchWord,
            @RequestParam(value = "countPerPage", defaultValue = DEFAULT_COUNT_PER_PAGE) int countPerPage,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponseService.toResponseEntity(productTicketBundleService.getProductTicketList(detailedConditionEnum, searchWord, countPerPage, page));
    }

}
