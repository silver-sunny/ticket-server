package com.kupstudio.bbarge.controller.store;

import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.store.StoreModifyRequestDto;
import com.kupstudio.bbarge.requestDto.store.StoreRequestDto;
import com.kupstudio.bbarge.service.store.StoreBundleService;
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
import javax.validation.constraints.Min;

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
@RequestMapping("/v1/api/store")
@Tag(name = "store-controller", description = "매장 API")
public class StoreController {

    private final StoreBundleService storeBundleService;

    @ApiResponse(description = "success - 성공 | 매장 정보 등록", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = int.class)))
    @Operation(summary = "매장 정보 등록 API", description = "매장을 등록합니다.<br><br>" +
            "[ 필수값 ]<br>" +
            "storeName: 매장명 - 글자수 제한: 1~14 | 영어, 한글만 입력 가능 (매장명 중복 체크 API 사용 후 입력하기)<br>" +
            "storeContactNumber: 매장 연락처 | (2 or 3)-(3 or 4)-(4) 자리의 연락처를 입력하세요.<br>" +
            "ex) 02-111-2345 | 05-1212-5678 | 010-222-1234 | 010-1234-5678<br>" +
            "storeAddress: 매장 주소<br><br>" +
            "[ 선택값 ]<br>" +
            "입력하지 않으시려면 <b>Request body</b> 에서 제거하십시오.<br>" +
            "companyName: 법인명<br>" +
            "taxpayerIdentificationNumber: 사업자 등록 번호 | 입력 시 000-00-00000 형식에 맞게 입력하세요.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @PostMapping(value = "")
    public ResponseEntity<?> insertStore(@RequestBody @Valid StoreRequestDto storeRequestDto) {

        int insertStoreNo = storeBundleService.insertStore(storeRequestDto);

        return ApiResponseService.toResponseEntity(insertStoreNo);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 매장 정보 조회", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StoreDto.class)))),
            @ApiResponse(responseCode = "490", description = "저장된 매장 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "특정 매장 정보 조회 API", description = "매장 정보 조회")
    @Parameter(name = "storeNo", description = "조회 할 매장 번호")
    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_MIDDLE')")
    @GetMapping("/{storeNo}")
    public ResponseEntity<?> getStoreDetail(@PathVariable(value = "storeNo") @Min(value = 1) int storeNo) {

        return ApiResponseService.toResponseEntity(storeBundleService.getStoreDetail(storeNo));

    }

    @ApiResponse(responseCode = "200", description = "success - 성공 | 매장 정보 리스트 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreDto.class)))
    @Operation(summary = "매장 정보 리스트 조회 API", description = "매장 정보 리스트")
    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_MIDDLE')")
    @GetMapping("")
    public ResponseEntity<?> getStoreList() {

        return ApiResponseService.toResponseEntity(storeBundleService.getStoreList());

    }


    @ApiResponses(value = {
            @ApiResponse(description = "success - 성공 | 매장 정보 수정", responseCode = "200", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "490", description = "저장된 매장 정보가 없습니다.", content = @Content)
    })
    @Operation(summary = "매장 정보 수정 API", description = "이전에 등록한 매장의 정보를 수정합니다.<br><br>" +
            "입력하지 않으시려면 <b>Request body</b> 에서 제거하십시오<br><br>" +
            "[ 필수값 ]<br>" +
            "storeContactNumber: 매장 연락처 | (2 or 3)-(3 or 4)-(4) 자리의 연락처를 입력하세요.<br>" +
            "ex) 02-111-2345 | 05-1212-5678 | 010-222-1234 | 010-1234-5678<br>" +
            "storeAddress: 매장 주소<br><br>" +
            "[ 선택값 ]<br>" +
            "입력하지 않으시려면 <b>Request body</b> 에서 제거하십시오.<br>" +
            "companyName: 법인명<br>" +
            "taxpayerIdentificationNumber: 사업자 등록 번호 | 입력 시 000-00-00000 형식에 맞게 입력하세요.")
    @Parameter(name = "storeNo", description = "수정 할 매장 번호")
    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_MIDDLE')")
    @PutMapping(value = "/{storeNo}")
    public ResponseEntity<?> updateStore(
            @PathVariable(value = "storeNo") @Min(value = 1) int storeNo,
            @RequestBody @Valid StoreModifyRequestDto storeModifyRequestDto
    ) {
        storeBundleService.updateStore(storeNo, storeModifyRequestDto);
        return ApiResponseService.toResponseEntity();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success - 성공 | 매장 정보 삭제", content = @Content),
            @ApiResponse(responseCode = "441", description = "판매 중인 상품이 있습니다.", content = @Content),
            @ApiResponse(responseCode = "450", description = "담당 운영자가 있습니다.", content = @Content),
            @ApiResponse(responseCode = "485", description = "해당 매장에 상품이 등록되어 있습니다.", content = @Content),
            @ApiResponse(responseCode = "490", description = "저장된 매장 정보가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "496", description = "판매 중인 상품과 담당 운영자가 있습니다.", content = @Content),
    })

    @Operation(summary = "매장 정보 삭제 API", description = "매장 정보 삭제")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @DeleteMapping("/{storeNo}")
    @Parameter(name = "storeNo", description = "삭제 할 매장 번호")
    public ResponseEntity<?> deleteStore(@PathVariable(value = "storeNo") @Min(value = 1) int storeNo) {

        storeBundleService.deleteStore(storeNo);
        return ApiResponseService.toResponseEntity();

    }

    @ApiResponse(responseCode = "200", description = "success - 성공 | 중복되는 storeName 존재 여부", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = boolean.class))))
    @Operation(summary = "storeName 중복 검사 API", description = "중복되는 storeName가 있는지 확인")
    @Parameter(name = "storeName", description = "중복 확인할 매장명")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @GetMapping("/storeName/{storeName}")
    public ResponseEntity<?> isDuplicateStoreName(@PathVariable(value = "storeName") String storeName) {

        return ApiResponseService.toResponseEntity(storeBundleService.isDuplicateStoreName(storeName));

    }

}
