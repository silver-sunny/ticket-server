package com.kupstudio.bbarge.service.product;

import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.dao.dbProduct.product.ProductDao;
import com.kupstudio.bbarge.dto.pagination.PageHelperDto;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.entity.product.ProductEntity;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;
    private final StoreService storeService;
    private final AdminService adminService;

    // 상품 등록
    public void insertProduct(ProductEntity productEntity) {
        String registrant = adminService.getAdminUserName();
        productEntity.setRegistrant(registrant);

        productDao.insertProduct(productEntity);
    }

    // 특정 상품 정보 조회
    public ProductDto getProductDetail(int productNo) {
        ProductDto productDto = productDao.getProductDetail(productNo);

        if (ObjectUtils.isEmpty(productDto)) {
            return new ProductDto();
        }

        return productDto;
    }

    // 특정 상품 정보 조회 + ChannelDetail 셋팅
    public ProductDto getProductDetailWithChannelDetailList(int productNo) {
        ProductDto productDto = getProductDetail(productNo);

        // 저장된 상품 정보가 없을 경우 exception
        if (ObjectUtils.isEmpty(productDto)) {
            return null;
        }

        // 삭제된 상품이라면 exception
        if (productDto.isDelete()) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_DELETE);
        }

        productDto.setChannelDetail(ChannelEnum.getChannelDetail(productDto));
        productDto.setStoreName(storeService.getStoreName(productDto.getStoreNo()));

        return productDto;
    }

    public void isValidProduct(int productNo) {
        ProductDto productDto = getProductDetail(productNo);

        // 존재하지 않는 상품이라면 exception
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
        }

        // 삭제된 상품이라면 exception
        if (productDto.isDelete()) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_DELETE);
        }
    }

    public ProductDto getProductDetailAndIsValidProductForPurchase(ProductOrderRequestDto requestDto) {
        ProductDto productDto = getProductDetail(requestDto.getProductNo());

        // 존재하지 않는 상품일 경우 exception
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
        }

        // 삭제된 상품일 경우 exception
        if (productDto.isDelete()) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_DELETE);
        }

        // 입력된 채널과 일치하지 않을 경우 경우 exception
        if (!productDto.getChannel().equals(requestDto.getChannelEnum())) {
            throw new ConditionFailException(ProductConstant.PURCHASE_CAN_ONLY_NEVERLAND_HC);
        }

        // 품절된 상품일 경우 exception
        if (productDto.getProductStateEnum().equals(ProductStateEnum.OUTOFSTOCK)) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_SOLD_OUT);
        }

        // 구매수량이 상품수량 보다 크다면 exception
        if (productDto.getStock() < requestDto.getPurchaseQuantity()) {
            throw new ConditionFailException(ProductConstant.PURCHASE_QUANTITY_TOO_LARGE);
        }

        return productDto;
    }

    // 상품 이름 조회
    public String getProductName(int productNo) {
        return getProductDetail(productNo).getProductName();
    }

    /**
     * 전체 상품 정보 리스트 조회
     *
     * @param countPerPage
     * @param page
     * @return
     */
    public PagingResponse getAllStoreProductList(int countPerPage, int page, Set<Integer> adminStoreNos) {

        int total = productDao.getTotal(adminStoreNos);

        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) return new PagingResponse(new ArrayList(), params);

        int offSet = (page - 1) * countPerPage;

        List<ProductDto> list = productDao.getProductList(countPerPage, offSet, adminStoreNos);

        for (ProductDto productDto : list) {
            productDto.setChannelDetail(ChannelEnum.getChannelDetail(productDto));
            productDto.setStoreName(storeService.getStoreName(productDto.getStoreNo()));
        }

        return new PagingResponse(list, params);
    }

    public void updateProduct(ProductEntity productEntity) {
        productDao.updateProduct(productEntity);
    }

    public boolean isExistProductByStoreNo(int storeNo) {
        return productDao.isExistProductByStoreNo(storeNo);
    }

    public void updateProductToDelete(int productNo) {
        productDao.updateProductToDelete(productNo);
    }

    public void updateProductStock(int productNo, int stock, ProductStateEnum productStateEnum) {
        productDao.updateProductStock(productNo, stock, productStateEnum);
    }
}
