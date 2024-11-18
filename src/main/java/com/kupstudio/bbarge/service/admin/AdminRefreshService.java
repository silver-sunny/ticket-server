package com.kupstudio.bbarge.service.admin;

import com.kupstudio.bbarge.dao.dbAdmin.AdminRefreshTokenDao;
import com.kupstudio.bbarge.dto.admin.AdminRefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRefreshService {

    private final AdminRefreshTokenDao adminRefreshTokenDao;


    public AdminRefreshTokenDto getRefreshTokenByAdminNo(int adminNo) {
        return adminRefreshTokenDao.getRefreshTokenByAdminNo(adminNo);
    }

    public AdminRefreshTokenDto getRefreshTokenByAdminId(String adminId) {
        return adminRefreshTokenDao.getRefreshTokenByAdminId(adminId);
    }

    public void insertUserRefreshToken(AdminRefreshTokenDto adminRefreshToken) {
        adminRefreshTokenDao.insertUserRefreshToken(adminRefreshToken);
    }

}
