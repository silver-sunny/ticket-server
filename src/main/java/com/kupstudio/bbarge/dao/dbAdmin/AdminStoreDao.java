package com.kupstudio.bbarge.dao.dbAdmin;

import com.kupstudio.bbarge.dto.admin.AdminStoreDto;
import com.kupstudio.bbarge.dto.admin.AdminStoreInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AdminStoreDao {


    List<AdminStoreDto> getStoreAdminByStoreNo(int storeNo);


    Set<Integer> getAdminStoreListByAdminNo(int adminNo);

    List<AdminStoreInfoDto> getAdminStoreInfoListByAdminNo(int adminNo);

    void insertAdminStoreNoList(@Param("adminNo") int adminNo,
                                @Param("storeNoList") List<Integer> storeNoList);

    void deleteStoreNoListByAdminNo(int adminNo);

    void deleteAdminNoListByStoreNo(@Param("storeNo") int store);

    boolean isExitAdminStore(@Param("adminNo") int adminNo);

    Set<Integer> getAdminListByStoreNo(@Param("storeNoSet") Set<Integer> storeNoSet);

    boolean isExitAdminStoreByStoreNo(@Param("storeNo") int storeNo);

    boolean isExitAdminStoreByAdminNoAndStoreNo(@Param("adminNo") int adminNo,
                                                @Param("storeNo") int storeNo);
}

