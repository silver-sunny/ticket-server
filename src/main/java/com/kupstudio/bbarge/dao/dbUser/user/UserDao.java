package com.kupstudio.bbarge.dao.dbUser.user;

import com.kupstudio.bbarge.dto.user.UserInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    UserInfoDto getUserInfo(@Param(value = "userNo") int userNo);
}
