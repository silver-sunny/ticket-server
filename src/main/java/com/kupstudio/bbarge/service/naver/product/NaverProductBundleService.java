package com.kupstudio.bbarge.service.naver.product;

import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.dao.naver.product.NaverProductDao;
import com.kupstudio.bbarge.dto.naver.product.NaverProductDto;
import com.kupstudio.bbarge.dto.naver.product.NaverProductResponseDto;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.entity.product.ProductEntity;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import com.kupstudio.bbarge.enumClass.product.naver.NaverMethodStateEnum;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.exception.product.NaverFailException;
import com.kupstudio.bbarge.requestDto.product.ProductModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductRequestDto;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.service.product.ProductService;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import static com.kupstudio.bbarge.constant.product.ProductConstant.PRODUCT_STATE_NOT_VALID;

@Service
@RequiredArgsConstructor
public class NaverProductBundleService {

    private final NaverProductDao naverProductDao;

    private final StoreService storeService;

    private final ProductService productService;

    private final AdminService adminService;

    /**
     * 폼에서 등록할때 판매중 -> 바로 등록
     * ''          판매 대기 - > 등록후 판매 대기로 수정
     * 네이버 등록, 수정
     *
     * @param productRequest
     * @return
     */
    public void insertOrUpdateNaverProductOfState(ProductRequestDto productRequest) {

        try {
            int storeNo = productRequest.getStoreNo();

            storeService.isValidStore(storeNo);

            ProductStateEnum productStateEnum = productRequest.getProductStateEnum();


            NaverMethodStateEnum naverMethodStateEnum = NaverMethodStateEnum.getNaverState(productStateEnum);

            switch (naverMethodStateEnum) {
                case INSERT:

                    // 등록된적이 없으면
                    // 처음 등록인데 판매중이면 insert
                    NaverProductResponseDto naverProductResponseDto = this.insertNaverProduct(productRequest);

                    // 자체 db에 저장
                    getNaverProductInfoAndInsert(storeNo, naverProductResponseDto);

                    break;
                case INSERT_AND_UPDATE:

                    // 판매 대기이면 판매중으로 네이버 insert
                    productRequest.setProductStateEnum(ProductStateEnum.SALE);
                    NaverProductResponseDto naverProductResponseDto2 = this.insertNaverProduct(productRequest);

                    // 판매 대기중으로 변경후 네이버 update
                    productRequest.setProductStateEnum(productStateEnum);
                    NaverProductResponseDto naverProductResponseDto1 = updateAfterInsertNaverProduct(productRequest, naverProductResponseDto2.getSmartstoreChannelProductNo());

                    // 자체 db에 저장
                    getNaverProductInfoAndInsert(storeNo, naverProductResponseDto1);

                    break;

                default:
                    // 해당이 다안되면 exception
                    throw new NaverFailException(PRODUCT_STATE_NOT_VALID);
            }


        } catch (Exception e) {
            throw new NaverFailException(e.getMessage());
        }

    }

    /**
     * 네이버 상품 삭제
     *
     * @param productNo
     */

    public void deleteNaverProduct(int productNo) {

        ProductDto productDto = productService.getProductDetail(productNo);

        // 저장된 상품 정보가 없을 경우 exception
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
        }

        // 네이버 API 에서 상품 삭제
        naverProductDao.deleteNaverProduct(productDto.getChannelProductId());

        // 자체 db에서 삭제
        productService.updateProductToDelete(productNo);

    }


    /**
     * ProductRequestDto - > NaverProductResponseDto 변환후
     * 네이버 상품 등록
     *
     * @param productDto
     * @return
     */
    public NaverProductResponseDto insertNaverProduct(ProductRequestDto productDto) {

        NaverProductDto naverProductDto = new NaverProductDto(productDto);

        return naverProductDao.insertNaverProduct(naverProductDto);
    }

    /**
     * 네이버 상품 추가후 -> 수정
     *
     * @param productDto
     * @param channelProductNo
     * @return
     */
    public NaverProductResponseDto updateAfterInsertNaverProduct(ProductRequestDto productDto, long channelProductNo) {

        NaverProductDto naverProductDto = new NaverProductDto(productDto);

        return naverProductDao.updateNaverProduct(naverProductDto, String.valueOf(channelProductNo));

    }

    /**
     * 네이버 상품 수정
     *
     * @param productModifyRequestDto
     * @return
     */
    public NaverProductResponseDto updateAfterInsertNaverProduct(ProductModifyRequestDto productModifyRequestDto) {

        ProductDto productDto = productService.getProductDetail(productModifyRequestDto.getProductNo());

        // 저장된 상품 정보가 없을 경우 exception
        if (ObjectUtils.isEmpty(productDto)) {
            throw new PostNotExistException(ProductConstant.PRODUCT_IS_NOT_EXIST);
            // 삭제된 상품이라면 exception
        } else if (productDto.isDelete()) {
            throw new ConditionFailException(ProductConstant.PRODUCT_IS_DELETE);
            // 유효하지 않은 상품 상태라면
        } else if (!ProductStateEnum.isValidProductStateForModify(productDto.getProductStateEnum())) {
            throw new ConditionFailException(ProductConstant.PRODUCT_MODIFY_BLOCK_BY_STATE);
        }

        int productNo = productModifyRequestDto.getProductNo();
        String channelProductNo = productDto.getChannelProductId();
        NaverProductDto naverProductDto = new NaverProductDto(productModifyRequestDto);

        // 네이버 수정
        NaverProductResponseDto naverProductResponseDto = naverProductDao.updateNaverProduct(naverProductDto, channelProductNo);

        // 수정후 값가져오기
        NaverProductDto naverProductRes = naverProductDao.getNaverProduct(channelProductNo);

        LocalDateTime modifyAt = LocalDateTime.now().withNano(0);
        ProductEntity productEntity = new ProductEntity().NaverProductToProductEntity(null, productNo, channelProductNo, naverProductRes, null, modifyAt);

        // 자체 db update
        productService.updateProduct(productEntity);

        return naverProductResponseDto;

    }


    /**
     * 네이버 상품 가져와 db에 저장
     *
     * @param storeNo
     * @param naverProductResponseDto
     * @return
     */
    public NaverProductDto getNaverProductInfoAndInsert(int storeNo, NaverProductResponseDto naverProductResponseDto) {

        String naverProductNo = String.valueOf(naverProductResponseDto.getSmartstoreChannelProductNo());
        NaverProductDto naverProductRes = naverProductDao.getNaverProduct(naverProductNo);

        LocalDateTime createAt = LocalDateTime.now().withNano(0);
        ProductEntity productEntity = new ProductEntity().NaverProductToProductEntity(storeNo, null, naverProductNo, naverProductRes, createAt, null);

        // 판매 채널: 자사 상품 등록
        productService.insertProduct(productEntity);
        return naverProductRes;
    }


}
