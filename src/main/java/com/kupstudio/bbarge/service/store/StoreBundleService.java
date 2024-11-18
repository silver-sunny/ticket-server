package com.kupstudio.bbarge.service.store;

import com.kupstudio.bbarge.constant.admin.AdminConstant;
import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.constant.store.StoreConstant;
import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.exception.admin.AdminStoreExistException;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.product.ProductExistException;
import com.kupstudio.bbarge.exception.store.StoreDeleteConditionFailException;
import com.kupstudio.bbarge.requestDto.store.StoreModifyRequestDto;
import com.kupstudio.bbarge.requestDto.store.StoreRequestDto;
import com.kupstudio.bbarge.service.admin.AdminBundleService;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.admin.AdminStoreService;
import com.kupstudio.bbarge.service.product.ProductService;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StoreBundleService {

    private final StoreService storeService;
    private final ProductService productService;
    private final AdminBundleService adminBundleService;
    private final AdminService adminService;
    private final AdminStoreService adminStoreService;


    // 매장 정보 등록
    public int insertStore(StoreRequestDto storeRequestDto) {

        String storeAddress = EmojiRemoveUtil.removeEmoji(storeRequestDto.getStoreAddress());

        String companyName = storeRequestDto.getCompanyName();
        if (StringUtils.isNotBlank(companyName)) {
            EmojiRemoveUtil.removeEmoji(companyName);
        } else {
            companyName = null;
        }

        // 매장명 중복 검사
        if (isDuplicateStoreName(storeRequestDto.getStoreName())) {
            throw new ConditionFailException(StoreConstant.STORE_NAME_DUPLICATE);
        }

        StoreDto storeDto = new StoreDto().StoreRequestDtoToStoreDto(storeRequestDto, storeAddress, companyName);

        storeService.insertStore(storeDto);

        return storeDto.getStoreNo();
    }

    // 매장 정보 수정
    public void updateStore(int storeNo, StoreModifyRequestDto storeModifyRequestDto) {

        // 유효한 매장 번호인지 확인
        storeService.isValidStore(storeNo);

        String storeAddress = EmojiRemoveUtil.removeEmoji(storeModifyRequestDto.getStoreAddress());

        String companyName = storeModifyRequestDto.getCompanyName();
        if (StringUtils.isNotEmpty(companyName)) {
            EmojiRemoveUtil.removeEmoji(companyName);
        } else {
            companyName = null;
        }

        StoreDto storeDto = new StoreDto().StoreModifyRequestDtoToStoreDto(storeModifyRequestDto, storeNo, storeAddress, companyName);

        storeService.updateStore(storeDto);
    }

    // 매장 삭제
    public void deleteStore(int storeNo) {

        storeService.isValidStore(storeNo);

        boolean isExistProduct = productService.isExistProductByStoreNo(storeNo);
        boolean isExistAdminStore = adminStoreService.isExitAdminStoreByStoreNo(storeNo);

        if (isExistProduct && isExistAdminStore) {
            throw new StoreDeleteConditionFailException(StoreConstant.STORE_DELETE_IS_NOT_VALID);
        }

        // 해당 매장에 상품이 등록 되어 있다면 삭제 불가
        if (isExistProduct) {
            throw new ProductExistException(ProductConstant.PRODUCT_IS_REGISTERED);
        }

        // 해당 매장을 담당하고 있는 운영자가 있다면 삭제 불가
        if (isExistAdminStore) {
            throw new AdminStoreExistException(AdminConstant.ADMIN_STORE_IS_EXITS);
        }

        // 삭제된 운영자의 담당 매장 목록에서 삭제
        adminStoreService.deleteAdminNoListByStoreNo(storeNo);
        // 매장 is_delete 로 변경
        storeService.updateStoreToDelete(storeNo);
    }

    public boolean isDuplicateStoreName(String storeName) {
        return storeService.isDuplicateStoreName(storeName);
    }

    // 매장 정보 조회
    public StoreDto getStoreDetail(int storeNo) {
        // 로그인한 운영자가 해당 매장을 담당 중인지 체크
        adminBundleService.isValidAdminStoreNo(storeNo);

        // 로그인한 운영자가 루트 운영자 이거나 중간 운영자 이면서 담당 매장이 있을 경우
        return storeService.getStoreDetail(storeNo);
    }

    public List<StoreDto> getStoreList() {
        Set<Integer> adminStoreNos = adminStoreService.getAdminStoreListByAdminNo(adminService.getAuthenticatedAdminNo());

        return storeService.getStoreList(adminStoreNos);
    }
}
