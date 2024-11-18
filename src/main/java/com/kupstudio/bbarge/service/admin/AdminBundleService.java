package com.kupstudio.bbarge.service.admin;

import com.kupstudio.bbarge.constant.admin.AdminConstant;
import com.kupstudio.bbarge.dto.admin.*;
import com.kupstudio.bbarge.dto.pagination.PageHelperDto;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.exception.admin.AdminStoreNotExistException;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.requestDto.admin.AdminRequestDto;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kupstudio.bbarge.constant.admin.AdminConstant.*;
import static com.kupstudio.bbarge.constant.store.StoreConstant.STORE_IS_NOT_EXIST;
import static com.kupstudio.bbarge.utils.ValidationUtil.adminIdValidate;
import static com.kupstudio.bbarge.utils.ValidationUtil.adminPasswordValidate;

@Service
@RequiredArgsConstructor
public class AdminBundleService {


    private final AdminService adminService;

    private final PasswordEncoder passwordEncoder;

    private final StoreService storeService;

    private final AdminStoreService adminStoreService;

    /**
     * ROOT 관리자가
     * 중간 관리자 등록 (ROLE_MIDDLE)
     * 매장 등록 선택
     *
     * @param requestDto
     */
    public void insertMiddleAdmin(AdminRequestDto requestDto) {


        String adminId = requestDto.getAdminId();

        if (!adminIdValidate(adminId)) {
            throw new ConditionFailException(ADMIN_ID_CONDITION_FAIL);
        }

        List<Integer> storeList = requestDto.getStoreNoList();

        // 요청한 있으면 추가
        if (!storeList.isEmpty()) {

            // 등록한 매장인지 여부 확인
            if (storeService.isIncludeStoreList(storeList)) {
                throw new ConditionFailException(STORE_IS_NOT_EXIST);
            }

        }

        // 관리자 아이디 중복되는지 확인
        if (adminService.isDuplicatedAdminId(requestDto.getAdminId())) {
            throw new ConditionFailException(ADMIN_ID_DUPLICATED);
        }

        requestDto.setPassword(passwordEncoder.encode(ADMIN_DEFAULT_PASSWORD));
        requestDto.setRole(AdminRoleEnum.ROLE_MIDDLE);

        adminService.insertAdmin(requestDto);


        Integer adminNo = requestDto.getAdminNo();


        if (adminNo == null) {
            throw new ConditionFailException(ADMIN_INSERT_FAIL);
        }

        adminStoreService.insertAdminStoreNoList(adminNo, storeList);


    }


    /**
     * 매장 운영자 등록 (ROLE_STORE)
     * 루트 권한 운영자 등록이 아님
     * 매장 등록 필수
     *
     * @param requestDto
     */
    public void insertStoreAdmin(AdminRequestDto requestDto) {


        List<Integer> storeList = requestDto.getStoreNoList();

        String adminId = requestDto.getAdminId();

        // 중간관리자만 하위관리자 등록 가능
        if (!adminService.isLoginAdminRoleHasRequestRole(AdminRoleEnum.ROLE_MIDDLE)) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }


        // 아이디 유효성 검사 확인
        if (!adminIdValidate(adminId)) {
            throw new ConditionFailException(ADMIN_ID_CONDITION_FAIL);
        }

        // 관리자 아이디 중복되는지 확인
        if (adminService.isDuplicatedAdminId(requestDto.getAdminId())) {
            throw new ConditionFailException(ADMIN_ID_DUPLICATED);
        }


        // 하위 운영자는 1개 매장 등록 필수
        if (storeList.size() != 1) {
            throw new ConditionFailException(ADMIN_STORE_ONLY_ONE_STORE);
        }

        Set<Integer> adminStore = adminStoreService.getAdminStore(adminService.getAuthenticatedAdminNo());

        // 운영자 등록할때 매장이 등록하려는 운영자의 관리 매장인지 확인
        if (!adminStore.contains(storeList.get(0))) {
            throw new ConditionFailException(ADMIN_ROLL_MIDDLE_HAS_NOT_STORE);
        }


        requestDto.setPassword(passwordEncoder.encode(ADMIN_DEFAULT_PASSWORD));
        requestDto.setRole(AdminRoleEnum.ROLE_STORE);

        adminService.insertAdmin(requestDto);


        Integer adminNo = requestDto.getAdminNo();


        if (adminNo == null) {
            throw new ConditionFailException(ADMIN_INSERT_FAIL);
        }


        adminStoreService.insertAdminStoreNoList(adminNo, storeList);


    }

    /**
     * ROOT 관리자가
     * MIDDLE 관리자
     * 매장 추가
     *
     * @param adminNo
     * @param storeNoList
     */
    public void updateMiddleAdminStoreList(int adminNo, List<Integer> storeNoList) {

        // ROOT 계정이 아니면 중간관리자 매장 추가할수 없음
        if (!adminService.isLoginAdminRoleHasRequestRole(AdminRoleEnum.ROLE_ROOT)) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }

        // 등록된 매장인지 여부
        if (storeService.isIncludeStoreList(storeNoList)) {
            throw new ConditionFailException(STORE_IS_NOT_EXIST);
        }

        AdminDto adminDto = adminService.getAdmin(adminNo);

        // 변경하려는 운영자가 중간 운영자가 아니라면 접근 불가능
        if (!AdminRoleEnum.ROLE_MIDDLE.equals(adminDto.getRole())) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }

        adminStoreService.deleteStoreNoListByAdminNo(adminNo);
        adminStoreService.insertAdminStoreNoList(adminNo, storeNoList);
    }

    /**
     * MIDDLE 관리자가
     * STORE 관리자
     * 하나 매장 추가
     *
     * @param adminNo
     * @param storeNo
     */
    public void updateStoreAdminStoreList(int adminNo, int storeNo) {


        AdminDto adminDto = adminService.getAdmin(adminNo);

        // 수정하려는 운영자가 하위관리자여야만됨
        if (!AdminRoleEnum.ROLE_STORE.equals(adminDto.getRole())) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }

        if (!canLoginAdminAccessAdmin(adminDto)) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }


        adminStoreService.deleteStoreNoListByAdminNo(adminNo);
        adminStoreService.insertAdminStoreNoList(adminNo, List.of(storeNo));

    }

    /**
     * @param adminNo
     * @return
     */
    public AdminInfoWithStoreDto getAdminInfoWithStore(Integer adminNo) {


        // 자기 정보
        if (adminNo == null) {
            adminNo = adminService.getAuthenticatedAdminNo();

            AdminDto admin = adminService.getAdmin(adminNo);


            if (admin == null) {
                throw new ConditionFailException(ADMIN_NOT_EXIST);

            }

            if (AdminRoleEnum.ROLE_ROOT.equals(admin.getRole())) {

                List<StoreDto> storeList = storeService.getStoreList();

                List<AdminStoreInfoDto> adminStoreList = storeList.stream()
                        .map(storeDto -> {
                            AdminStoreInfoDto adminStoreInfoDto = new AdminStoreInfoDto();
                            adminStoreInfoDto.setStoreNo(storeDto.getStoreNo());
                            adminStoreInfoDto.setStoreName(storeDto.getStoreName());
                            return adminStoreInfoDto;
                        })
                        .collect(Collectors.toList());


                return new AdminInfoWithStoreDto(admin, adminStoreList);

            } else {
                return new AdminInfoWithStoreDto(admin, adminStoreService.getAdminStoreInfoListByAdminNo(adminNo));

            }


        } else {
            // 검색한 운영자 정보
            AdminDto admin = adminService.getAdmin(adminNo);

            // 검색한 관리자한테 접근 가능한지 여부
            if (!canLoginAdminAccessAdmin(admin)) {
                throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
            }
            return new AdminInfoWithStoreDto(admin, adminStoreService.getAdminStoreInfoListByAdminNo(adminNo));


        }

    }


    /**
     * 비밀번호 초기화
     *
     * @param adminNo
     */
    public void resetPassword(int adminNo) {


        AdminDto admin = adminService.getAdmin(adminNo);

        if (!canLoginAdminAccessAdmin(admin)) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }

        this.updatePassWordWithEncode(adminNo, ADMIN_DEFAULT_PASSWORD);

    }

    /**
     * 비밀번호 변경
     *
     * @param password
     */
    public void updateLoginAdminPassword(String password) {

        if (!adminPasswordValidate(password)) {
            throw new ConditionFailException(ADMIN_PASSWORD_CONDITION_FAIL);
        }
        int loginAdminNo = adminService.getAuthenticatedAdminNo();
        this.updatePassWordWithEncode(loginAdminNo, password);
    }

    /**
     * 암호화 적용된 비밀번호
     *
     * @param adminNo
     * @param password
     */
    public void updatePassWordWithEncode(int adminNo, String password) {
        adminService.updatePassword(adminNo, passwordEncoder.encode(password));

    }


    public PagingResponse getAdminListWithStore(
            AdminRoleEnum searchRole,
            Integer searchStoreNo,
            int countPerPage, int page) {


        AdminRoleEnum adminRole = adminService.getLoginAdminInfo().getRole();

        // 검색하려는 운영자 리스트
        Set<Integer> adminList = null;

        switch (adminRole) {
            case ROLE_ROOT:
                // 검색하는 store가 있을 경우
                if (searchStoreNo != null) {
                    adminList = adminStoreService.getAdminListByStoreNo(Set.of(searchStoreNo));
                }
                break;

            case ROLE_MIDDLE:

                // 검색하는 store가 있을 경우
                if (searchStoreNo != null) {
                    if (!adminStoreService.getAdminStore(adminService.getAuthenticatedAdminNo()).contains(searchStoreNo)) {
                        throw new AccessDeniedException(ADMIN_ROLL_MIDDLE_HAS_NOT_STORE);
                    }
                    adminList = adminStoreService.getAdminListByStoreNo(Set.of(searchStoreNo));

                } else {
                    Set<Integer> loginStoreSet = adminStoreService.getAdminStore(adminService.getAuthenticatedAdminNo());
                    if (loginStoreSet.isEmpty()) {
                        return new PagingResponse<>(Collections.emptyList(), new PageHelperDto(countPerPage, page, 0, null));

                    }

                    adminList = adminStoreService.getAdminListByStoreNo(loginStoreSet);
                }

                break;
            case ROLE_STORE:
                throw new AccessDeniedException(ADMIN_NOT_ALLOWED);


        }


        if (searchStoreNo != null && searchStoreNo > 0 && (adminList == null || adminList.isEmpty())) {
            return new PagingResponse<>(Collections.emptyList(), new PageHelperDto(countPerPage, page, 0, null));
        }

        if (!adminService.isLoginAdminRoleHasRequestRole(AdminRoleEnum.ROLE_ROOT)) {
            searchRole = AdminRoleEnum.ROLE_STORE;
        }


        int total = adminService.getAdminListWithStoreTotal(searchRole, adminList, searchStoreNo);
        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) return new PagingResponse<>(Collections.emptyList(), params);

        int offSet = (page - 1) * countPerPage;

        List<AdminInfoListWithStoreDto> adminInfoList = adminService.getAdminListWithStore(searchRole, adminList, searchStoreNo, countPerPage, offSet);

        for (AdminInfoListWithStoreDto adminInfo : adminInfoList) {
            adminInfo.setStoreList(adminStoreService.getAdminStoreInfoListByAdminNo(adminInfo.getAdminNo()));
        }
        return new PagingResponse(adminInfoList, params);
    }

    public void updateAdminByIsDelete(int adminNo) {

        AdminDto admin = adminService.getAdmin(adminNo);


        if (!canLoginAdminAccessAdmin(admin)) {
            throw new AccessDeniedException(ADMIN_NOT_ALLOWED);
        }


        adminService.updateAdminByIsDelete(adminNo, true);
    }

    /**
     * 해당 운영자한테 접근 가능한지 여부
     *
     * @param adminDto
     * @return
     */
    private boolean canLoginAdminAccessAdmin(AdminDto adminDto) {

        if (adminDto == null) {
            throw new ConditionFailException(ADMIN_NOT_EXIST);
        }

        AdminRoleEnum loginAdminRole = adminService.getLoginAdminInfo().getRole();

        AdminRoleEnum adminRole = adminDto.getRole();

        // 최상위 관리자면 접근 불가
        if (AdminRoleEnum.ROLE_ROOT.equals(adminRole)) {
            return false;
        }

        switch (loginAdminRole) {
            case ROLE_ROOT:
                return true;
            case ROLE_MIDDLE:
                // 중간 관리자인데 중간 관리자한테 접근 못함
                if (AdminRoleEnum.ROLE_MIDDLE.equals(adminRole)) {
                    return false;
                }
                return checkMiddleRole(adminDto.getAdminNo());
            default:
                return false;
        }
    }

    /**
     * @param adminNo
     * @return
     */
    private boolean checkMiddleRole(int adminNo) {
        Set<Integer> loginStoreSet = adminStoreService.getAdminStore(adminService.getAuthenticatedAdminNo());

        // 담당하고 있는 매장이 없으면 접근 불가
        if (loginStoreSet.isEmpty()) {
            return false;
        }

        Set<Integer> searchAdminStoreSet = adminStoreService.getAdminStore(adminNo);
        return loginStoreSet.contains(searchAdminStoreSet.iterator().next());
    }


    // 로그인한 운영자의 담당 매장 보유 여부 체크
    public boolean isLoginAdminHasStore() {
        AdminAuthDto adminInfo = adminService.getLoginAdminInfo();

        // 루트 운영자일 경우 ture
        if (adminInfo.getRole().equals(AdminRoleEnum.ROLE_ROOT)) {
            return true;
        } else {
            // 루트 운영자가 아닐 경우 운영자의 담당 매장 보유 여부 체크
            return adminStoreService.isExitAdminStore(adminInfo.getAdminNo());
        }
    }

    // 로그인한 운영자의 담당 매장이 없을 경우 에러
    public void isHasAdminStore() {
        if (!isLoginAdminHasStore()) {
            throw new AdminStoreNotExistException(AdminConstant.ADMIN_NOT_ALLOWED);
        }
    }

    // 로그인한 운영자가 해당 매장을 담당 하고 있지 않다면 에러
    public boolean isLoginAdminHasStoreByStoreNo(int storeNo) {
        AdminAuthDto adminInfo = adminService.getLoginAdminInfo();

        if (adminInfo.getRole().equals(AdminRoleEnum.ROLE_ROOT)) {
            return true;
        } else {
            // 루트 운영자가 아닐 경우 운영자의 담당 매장 보유 여부 체크
            return adminStoreService.isExitAdminStoreByAdminNoAndStoreNo(adminInfo.getAdminNo(), storeNo);
        }
    }

    // 로그인한 운영자가 해당 매장을 담당하고 있지 않다면 에러
    public void isValidAdminStoreNo(int storeNo) {
        if (!isLoginAdminHasStoreByStoreNo(storeNo)) {
            throw new AdminStoreNotExistException(AdminConstant.ADMIN_ROLL_MIDDLE_HAS_NOT_STORE);
        }
    }

}
