package com.kupstudio.bbarge.dao.dbAdmin.store;

import com.kupstudio.bbarge.dto.store.StoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface StoreDao {
    void insertStore(StoreDto storeDto);

    StoreDto getStoreDetail(@Param(value = "storeNo") int storeNo);

    List<StoreDto> getStoreList(@Param(value = "adminStoreNos") Set<Integer> adminStoreNos,
                                @Param(value = "isForStore") boolean isForStore);

    void updateStore(StoreDto storeDto);

    void deleteStore(@Param(value = "storeNo") int storeNo);

    boolean isDuplicateStoreName(@Param(value = "storeName") String storeName);

    void updateStoreToDelete(@Param(value = "storeNo") int storeNo);
}
