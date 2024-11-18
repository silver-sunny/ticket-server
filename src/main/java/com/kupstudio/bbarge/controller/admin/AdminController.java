package com.kupstudio.bbarge.controller.admin;


import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.exceptionService.ApiResponseService;
import com.kupstudio.bbarge.requestDto.admin.AdminRequestDto;
import com.kupstudio.bbarge.service.admin.AdminBundleService;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.admin.AdminStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "bad request -잘못된 형식의 구문으로 인해 서버에서 요청을 이해할 수 없습니다.", content = @Content),
        @ApiResponse(responseCode = "401", description = "unauthorized - 인증되지 않은 사용자입니다.", content = @Content),
        @ApiResponse(responseCode = "403", description = "forbidden - 권한이 없습니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found - 존재하지 않는 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed - 허용되지 않은 메소드 요청입니다.", content = @Content),
        @ApiResponse(responseCode = "422", description = "Validation fail - 데이터 타입을 확인해 주세요.", content = @Content)})
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AdminBundleService adminBundleService;
    private final AdminStoreService adminStoreService;

    @Operation(summary = "매장 운영자 계정 생성", description = "중간 운영자가 매장 운영자를 생성합니다.<br>" +
            "매장 운영자는 하나의 매장만 관리할 수 있습니다. storeNo 하나만 입력해야 합니다.")
    @PreAuthorize("hasAnyRole('ROLE_MIDDLE')")
    @PostMapping(value = "/store-admin")
    public ResponseEntity<?> insertAdmin(@RequestBody AdminRequestDto adminDto) {

        adminBundleService.insertStoreAdmin(adminDto);
        return ApiResponseService.toResponseEntity();
    }

    @Operation(summary = "중간 관리자 계정 생성", description = "루트 권한의 운영자가 중간 관리자 권한 운영자를 생성합니다.<br>" +
            "중간 관리자는 하나 이상의 매장을 관리할 수 있습니다. storeNo 여러개를 입력할 수 있습니다.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @PostMapping(value = "/middle-admin")
    public ResponseEntity<?> insertMiddleAdmin(@RequestBody AdminRequestDto adminDto) {

        adminBundleService.insertMiddleAdmin(adminDto);
        return ApiResponseService.toResponseEntity();
    }

    @Operation(summary = "운영자 계정 생성 시 아이디 중복확인", description = "중복된 ID 라면 true<br>중복되지 않은 ID (생성 가능한 ID) 라면 false 를 반환합니다.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @GetMapping(value = "/is-duplicated-id")
    public ResponseEntity<?> isDuplicatedAdminId(@RequestParam String adminId) {

        return ApiResponseService.toResponseEntity(adminService.isDuplicatedAdminId(adminId));
    }

    @Operation(summary = "매장 운영자 목록", description = "storeNo를 받아 해당 매장의 운영자(ROLE_USER) 목록을 반환합니다.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @GetMapping(value = "/{storeNo}")
    public ResponseEntity<?> getStoreAdminByStoreNo(@PathVariable(value = "storeNo") int storeNo) {

        return ApiResponseService.toResponseEntity(adminStoreService.getStoreAdminByStoreNo(storeNo));
    }

    @Operation(summary = "ROOT 관리자가 MIDDLE 관리자 store 변경", description = "ROOT 관리자가 MIDDLE관리자한체 store 추가")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @PutMapping(value = "/middle-admin/store/{adminNo}")
    public ResponseEntity<?> updateMiddleAdminStoreList(@PathVariable int adminNo,
                                                        @RequestParam List<Integer> storeNoList) {

        adminBundleService.updateMiddleAdminStoreList(adminNo, storeNoList);
        return ApiResponseService.toResponseEntity();
    }


    @Operation(summary = "루트, 중간 운영자가 하위운영자 매장 변경", description = "루트, 중간 운영자가 하위 운영자 매장 변경<br>" +
            " 중간운영자는 관리하는 매장만 하위관리자의 매장에 추가할수 있습니다.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @PutMapping(value = "/store-admin/store/{adminNo}")
    public ResponseEntity<?> updateStoreAdminStoreList(@PathVariable int adminNo,
                                                       @RequestParam int storeNo) {

        adminBundleService.updateStoreAdminStoreList(adminNo, storeNo);
        return ApiResponseService.toResponseEntity();
    }


    @Operation(summary = "로그인한 운영자 매장 리스트 출력", description = "로그인한 운영자 매장 리스트 출력")
    @GetMapping(value = "/store")
    public ResponseEntity<?> getAdminStoreList() {


        return ApiResponseService.toResponseEntity(adminStoreService.getLoginAdminStoreInfoList());
    }

    @Operation(summary = "로그인한 운영자 비밀번호 변경", description = "로그인한 운영자 비밀번호 변경")
    @PutMapping(value = "/password")
    public ResponseEntity<?> updateLoginAdminPassword(@RequestParam String password) {


        adminBundleService.updateLoginAdminPassword(password);
        return ApiResponseService.toResponseEntity();
    }


    @Operation(summary = "사용자 비밀번호 초기화", description = "사용자 비밀번호 초기화 <br>" +
            " 하위관리자는 -> 최상위, 중간 운영자만 초기화 가능 <br>" +
            " 중간관리자는 -> 하위만 초기화 가능")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @PutMapping(value = "/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam int adminNo) {


        adminBundleService.resetPassword(adminNo);
        return ApiResponseService.toResponseEntity();
    }

    @Operation(summary = "운영자 정보 , 담당매장 리스트", description = "운영자 정보")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @GetMapping(value = "/admin-info")
    public ResponseEntity<?> getAdminInfo(@RequestParam(required = false) Integer adminNo) {


        return ApiResponseService.toResponseEntity(adminBundleService.getAdminInfoWithStore(adminNo));
    }


    @Operation(summary = "운영자관리 > 운영자 조회 리스트 페이징", description = "운영자 조회 리스트")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @GetMapping(value = "")
    public ResponseEntity<?> getAdminListWithStore(@RequestParam(required = false) AdminRoleEnum adminRoleEnum,
                                                   @RequestParam(required = false) Integer searchStoreNo,
                                                   @RequestParam(required = false, defaultValue = "10") int page,
                                                   @RequestParam(required = false, defaultValue = "1") int currentPage
    ) {


        return ApiResponseService.toResponseEntity(adminBundleService.getAdminListWithStore(adminRoleEnum, searchStoreNo, page, currentPage));


    }

    @Operation(summary = "운영자관리 > 운영자 조회 > 삭제", description = "루트, 중간 운영자만 삭제가능합니다.")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_MIDDLE')")
    @DeleteMapping(value = "/{adminNo}")
    public ResponseEntity<?> deleteAdmin(@PathVariable int adminNo) {

        adminBundleService.updateAdminByIsDelete(adminNo);
        return ApiResponseService.toResponseEntity();


    }


}