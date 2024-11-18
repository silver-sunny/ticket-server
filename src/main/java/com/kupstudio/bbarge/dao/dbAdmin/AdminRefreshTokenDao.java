package com.kupstudio.bbarge.dao.dbAdmin;

import com.kupstudio.bbarge.dto.admin.AdminRefreshTokenDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminRefreshTokenDao {

    AdminRefreshTokenDto getRefreshTokenByAdminNo(int adminNo);

    AdminRefreshTokenDto getRefreshTokenByAdminId(String adminId);


    void insertUserRefreshToken(AdminRefreshTokenDto adminRefreshToken);
}