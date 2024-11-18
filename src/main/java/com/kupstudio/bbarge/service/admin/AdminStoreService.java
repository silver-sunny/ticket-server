package com.kupstudio.bbarge.service.admin;

import com.kupstudio.bbarge.dao.dbAdmin.AdminStoreDao;
import com.kupstudio.bbarge.dto.admin.AdminStoreDto;
import com.kupstudio.bbarge.dto.admin.AdminStoreInfoDto;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.exception.common.DeleteFailException;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreService {

    private final AdminStoreDao adminStoreDao;

    private final AdminService adminService;

    private final StoreService storeService;

    /**
     * storeNo를 관리하고있는 관리자 리스트
     *
     * @param storeNo
     * @return
     */
    public List<AdminStoreDto> getStoreAdminByStoreNo(int storeNo) {
        return adminStoreDao.getStoreAdminByStoreNo(storeNo);
    }

    public Set<Integer> getAdminStore(int adminNo) {
        return adminStoreDao.getAdminStoreListByAdminNo(adminNo);
    }

    public void insertAdminStoreNoList(int adminNo, List<Integer> storeNoList) {
        adminStoreDao.insertAdminStoreNoList(adminNo, storeNoList);
    }

    public void deleteStoreNoListByAdminNo(int adminNo) {
        adminStoreDao.deleteStoreNoListByAdminNo(adminNo);
    }

    public void deleteAdminNoListByStoreNo(int store) {
        try {
            adminStoreDao.deleteAdminNoListByStoreNo(store);
        } catch (Exception e) {
            throw new DeleteFailException();
        }
    }

    public List<AdminStoreInfoDto> getLoginAdminStoreInfoList() {
        int adminNo = adminService.getAuthenticatedAdminNo();

        if (adminService.isLoginAdminRoleHasRequestRole(AdminRoleEnum.ROLE_ROOT)) {

            List<StoreDto> storeList = storeService.getStoreList();

            return storeList.stream()
                    .map(storeDto -> {
                        AdminStoreInfoDto adminStoreInfoDto = new AdminStoreInfoDto();
                        adminStoreInfoDto.setStoreNo(storeDto.getStoreNo());
                        adminStoreInfoDto.setStoreName(storeDto.getStoreName());
                        return adminStoreInfoDto;
                    })
                    .collect(Collectors.toList());
        } else {

            return this.getAdminStoreInfoListByAdminNo(adminNo);
        }
    }

    // 운영자가 담당하고 있는 매장 번호 리스트 조회
    public Set<Integer> getAdminStoreListByAdminNo(int adminNo) {
        return adminStoreDao.getAdminStoreListByAdminNo(adminNo);
    }

    // 운영자의 담당 매장 보유 여부 체크
    public boolean isExitAdminStore(int adminNo) {
        return adminStoreDao.isExitAdminStore(adminNo);
    }

    public boolean isExitAdminStoreByStoreNo(int storeNo) {
        return adminStoreDao.isExitAdminStoreByStoreNo(storeNo);
    }

    public List<AdminStoreInfoDto> getAdminStoreInfoListByAdminNo(int adminNo) {
        return adminStoreDao.getAdminStoreInfoListByAdminNo(adminNo);
    }

    public Set<Integer> getAdminListByStoreNo(Set<Integer> storeNoList) {
        return adminStoreDao.getAdminListByStoreNo(storeNoList);
    }

    // 해당 운영자가 해당 매장을 담당하고 있는지 체크
    public boolean isExitAdminStoreByAdminNoAndStoreNo(int adminNo, int storeNo) {
        return adminStoreDao.isExitAdminStoreByAdminNoAndStoreNo(adminNo, storeNo);
    }
}
