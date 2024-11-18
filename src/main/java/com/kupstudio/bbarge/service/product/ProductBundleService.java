package com.kupstudio.bbarge.service.product;

import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.constant.product.ProductOrderConstant;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.user.UserInfoDto;
import com.kupstudio.bbarge.entity.product.ProductEntity;
import com.kupstudio.bbarge.entity.product.ProductOrderEntity;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductOrderRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import com.kupstudio.bbarge.service.admin.AdminBundleService;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.admin.AdminStoreService;
import com.kupstudio.bbarge.service.product.order.ProductOrderService;
import com.kupstudio.bbarge.service.user.UserService;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductBundleService {

    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final UserService userService;
    private final AdminBundleService adminBundleService;
    private final AdminService adminService;
    private final AdminStoreService adminStoreService;


    // 상품 등록
    public Integer insertProduct(ProductRequestDto productRequestDto) {

        ProductEntity productEntity = new ProductEntity().ProductRequestDtoToProductEntity(productRequestDto);

        // 판매 채널: 자사 상품 등록
        productEntity.setChannelNo(ChannelEnum.NEVERLAND_HC.getIndex());
        productService.insertProduct(productEntity);

        return productEntity.getProductNo();
    }

    // 상품 정보 상세 조회
    public ProductDto getProductDetail(int productNo) {

        return productService.getProductDetailWithChannelDetailList(productNo);
    }

    // 상품 수정
    public void updateProduct(ProductModifyRequestDto productModifyRequestDto) {

        ProductDto productDto = productService.getProductDetail(productModifyRequestDto.getProductNo());

        // 존재하지 않는 상품이라면 exception
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
            // 삭제된 상품이라면 exception
        } else if (productDto.isDelete()) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_DELETE);
            // 유효하지 않은 상품 상태라면
        } else if (!ProductStateEnum.isValidProductStateForModify(productDto.getProductStateEnum())) {
            throw new ConditionFailException(ProductConstant.PRODUCT_MODIFY_BLOCK_BY_STATE);
        }

        ProductEntity productEntity = new ProductEntity().ProductModifyRequestDtoToProductEntity(productModifyRequestDto);

        productService.updateProduct(productEntity);
    }

    public void deleteProduct(int productNo) {

        // 존재하지 않는 상품이라면 exception
        productService.isValidProduct(productNo);

        // 해당 상품의 대한 주문 내역이 있을 경우 삭제 불가능
        if (productOrderService.isExistOrderByProductNo(productNo)) {
            throw new ConditionFailException(ProductOrderConstant.PRODUCT_IS_EXIST_ORDER);
        }

        productService.updateProductToDelete(productNo);
    }

    // 메서드 수준 동기화 사용하여 동시성 제어
    public synchronized int purchaseProduct(ProductOrderRequestDto requestDto) {
        // 유효한 상품인지 확인
        int productNo = requestDto.getProductNo();
        ProductDto productDto = productService.getProductDetailAndIsValidProductForPurchase(requestDto);

        int stock = productDto.getStock();
        int purchaseQuantity = requestDto.getPurchaseQuantity();

        // 직접입력 옵션 이모지 제거
        String directOption = requestDto.getDirectOption();
        directOption = StringUtils.isNotBlank(directOption) ? EmojiRemoveUtil.removeEmoji(directOption) : null;

        // 사용자 정보 조회
        UserInfoDto userInfoDto = userService.getUserInfo(requestDto.getUserNo());

        stock -= purchaseQuantity;
        ProductStateEnum productStateEnum = (stock == 0) ? ProductStateEnum.OUTOFSTOCK : null;

        // 상품수량 업데이트
        productService.updateProductStock(productNo, stock, productStateEnum);

        // 주문 내역 추가
        ProductOrderEntity entity = ProductOrderEntity.ProductOrderRequestDtToProductOrderEntity(directOption, requestDto, productDto, userInfoDto);
        productOrderService.insertOrder(entity);

        return entity.getOrderNo();
    }

    public PagingResponse getAllStoreProductList(int countPerPage, int page) {
        // 로그인한 운영자의 담당 매장이 있는지 체크;
        adminBundleService.isHasAdminStore();

        Set<Integer> adminStoreNos = adminStoreService.getAdminStoreListByAdminNo(adminService.getAuthenticatedAdminNo());

        return productService.getAllStoreProductList(countPerPage, page, adminStoreNos);
    }
}
