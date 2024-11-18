package com.kupstudio.bbarge.service.store;

import com.kupstudio.bbarge.constant.store.StoreConstant;
import com.kupstudio.bbarge.dao.dbAdmin.store.StoreDao;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreDao storeDao;


    // 매장 정보 등록
    public void insertStore(StoreDto storeDto) {

        storeDao.insertStore(storeDto);
    }

    // 특정 매장 정보 조회
    public StoreDto getStoreDetail(int storeNo) {

        StoreDto storeDto = storeDao.getStoreDetail(storeNo);

        if (ObjectUtils.isEmpty(storeDto)) {
            return new StoreDto();
        }

        // 삭제된 상품이라면 exception
        if (storeDto.isDelete()) {
            throw new ConditionFailException(StoreConstant.STORE_IS_DELETE);
        }

        return storeDto;
    }

    public void isValidStore(int storeNo) {
        StoreDto storeDto = getStoreDetail(storeNo);

        // 존재하지 않는 매장이라면 exception
        if (ObjectUtils.isEmpty(storeDto)) {
            throw new PostNotExistException(StoreConstant.STORE_IS_NOT_EXIST);
        }

        // 삭제된 매장이라면 exception
        if (storeDto.isDelete()) {
            throw new ConditionFailException(StoreConstant.STORE_IS_DELETE);
        }
    }

    // 매장 정보 수정
    public void updateStore(StoreDto storeDto) {
        storeDao.updateStore(storeDto);
    }

    // 매장 정보 삭제
    public void deleteStore(int storeNo) {
        storeDao.deleteStore(storeNo);
    }

    public List<StoreDto> getStoreList(Set<Integer> adminStoreNos) {
        return storeDao.getStoreList(adminStoreNos, true);
    }

    public List<StoreDto> getStoreList() {
        return storeDao.getStoreList(null, false);

    }

    public String getStoreName(int storeNo) {
        return getStoreDetail(storeNo).getStoreName();
    }

    public boolean isDuplicateStoreName(String storeName) {
        return storeDao.isDuplicateStoreName(storeName);
    }

    /**
     * 요청한 매장번호가
     * 저장된 매장 리스트중에
     * 중복된게 없으면 return false
     *
     * @param requestStoreList
     * @return
     */
    public boolean isIncludeStoreList(List<Integer> requestStoreList) {


        List<StoreDto> storeList = this.getStoreList();

        List<Integer> notIncludedStoreNumbers = new ArrayList<>();

        for (int storeNo : requestStoreList) {
            boolean isUnique = true;
            for (StoreDto storeDto : storeList) {
                if (storeNo == storeDto.getStoreNo()) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                notIncludedStoreNumbers.add(storeNo);
            }
        }

        return !notIncludedStoreNumbers.isEmpty();
    }

    public void updateStoreToDelete(int storeNo) {
        storeDao.updateStoreToDelete(storeNo);
    }
}
