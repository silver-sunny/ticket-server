package com.kupstudio.bbarge.service.admin;

import com.kupstudio.bbarge.dao.dbAdmin.AdminDao;
import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.dto.admin.AdminDto;
import com.kupstudio.bbarge.dto.admin.AdminInfoListWithStoreDto;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.requestDto.admin.AdminRequestDto;
import com.kupstudio.bbarge.security.admin.AdminPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.kupstudio.bbarge.constant.admin.AdminConstant.ADMIN_ID_CONDITION_FAIL;
import static com.kupstudio.bbarge.utils.ValidationUtil.adminIdValidate;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDao adminDao;

    /**
     * adminNo로 운영자 정보 출력
     *
     * @param adminNo
     * @return
     */
    public AdminDto getAdmin(int adminNo) {
        return adminDao.getAdmin(adminNo);
    }

    /**
     * 로그인한 admin No 조회
     */
    public int getAuthenticatedAdminNo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        return admin.getAdminNo();
    }


    /**
     * 로그인한 사용자 정보
     *
     * @return
     */
    public AdminAuthDto getLoginAdminInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        return admin.getAdminDto();
    }


    public AdminAuthDto getAdminInfoByAdminId(String adminId) {
        return adminDao.getAdminInfoByAdminId(adminId);
    }

    /**
     * 운영자 계정 생성
     *
     * @param adminDto
     */
    public void insertAdmin(AdminRequestDto adminDto) {

        adminDao.insertAdmin(adminDto);
    }

    public boolean isDuplicatedAdminId(String adminId) {

        if (!adminIdValidate(adminId)) {
            throw new ConditionFailException(ADMIN_ID_CONDITION_FAIL);
        }
        return adminDao.isDuplicatedAdminId(adminId);
    }

    public String getAdminUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 로그인한 관리자가
     * 요청한 관리자 권한과 일치 여부 출력
     * 맞으면 true 틀리면 false
     *
     * @param requestAdminRole
     * @return
     */
    public boolean isLoginAdminRoleHasRequestRole(AdminRoleEnum requestAdminRole) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> auths = auth.getAuthorities();

        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(requestAdminRole.name()));
    }

    public void updatePassword(int adminNo,
                               String password) {
        adminDao.updatePassword(adminNo, password);
    }

    public List<AdminInfoListWithStoreDto> getAdminListWithStore(
            AdminRoleEnum searchRole,
            Set<Integer> searchAdminNoSet,
            Integer storeNo,
            int countPerPage,
            int offSet) {

        return adminDao.getAdminListWithStore(AdminRoleEnum.ROLE_ROOT, searchRole, searchAdminNoSet, storeNo, countPerPage, offSet);
    }

    public int getAdminListWithStoreTotal(AdminRoleEnum searchRole, Set<Integer> adminSet, Integer storeNo) {

        return adminDao.getAdminListWithStoreTotal(AdminRoleEnum.ROLE_ROOT, searchRole, adminSet, storeNo);
    }

    public void updateAdminByIsDelete(int adminNo, boolean isDelete) {

        adminDao.updateAdminByIsDelete(adminNo, isDelete);
    }
}
