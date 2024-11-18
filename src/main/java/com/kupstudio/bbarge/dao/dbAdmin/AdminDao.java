package com.kupstudio.bbarge.dao.dbAdmin;

import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.dto.admin.AdminDto;
import com.kupstudio.bbarge.dto.admin.AdminInfoListWithStoreDto;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import com.kupstudio.bbarge.requestDto.admin.AdminRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AdminDao {

    AdminDto getAdmin(int adminNo);

    AdminAuthDto getAdminInfoByAdminId(String adminId);

    void insertAdmin(AdminRequestDto requestDto);

    boolean isDuplicatedAdminId(String adminId);


    void updatePassword(@Param("adminNo") int adminNo,
                        @Param("password") String password);

    List<AdminInfoListWithStoreDto> getAdminListWithStore(@Param("exceptRole") AdminRoleEnum exceptRole,
                                                          @Param("searchRole") AdminRoleEnum searchRole,
                                                          @Param("searchAdminNoSet") Set<Integer> searchAdminNoSet,
                                                          @Param("searchStoreNo") Integer searchStoreNo,
                                                          @Param(value = "countPerPage") int countPerPage,
                                                          @Param(value = "offSet") int offSet);

    int getAdminListWithStoreTotal(@Param("exceptRole") AdminRoleEnum exceptRole,
                                   @Param("searchRole") AdminRoleEnum searchRole,
                                   @Param("searchAdminNoSet") Set<Integer> searchAdminNoSet,
                                   @Param("searchStoreNo") Integer searchStoreNo);

    void updateAdminByIsDelete(@Param("adminNo") int adminNo,
                               @Param("isDelete") boolean isDelete);
}
