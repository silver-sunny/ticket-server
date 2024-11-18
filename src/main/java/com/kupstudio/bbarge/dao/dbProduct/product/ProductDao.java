package com.kupstudio.bbarge.dao.dbProduct.product;

import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.entity.product.ProductEntity;
import com.kupstudio.bbarge.enumClass.product.ProductStateEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductDao {
    void insertProduct(ProductEntity productEntity);

    ProductDto getProductDetail(@Param(value = "productNo") int productNo);

    int getTotal(@Param(value = "adminStoreNos") Set<Integer> adminStoreNos);

    List<ProductDto> getProductList(@Param(value = "countPerPage") int countPerPage,
                                    @Param(value = "offSet") int offSet,
                                    @Param(value = "adminStoreNos") Set<Integer> adminStoreNos);

    void updateProduct(ProductEntity productEntity);

    boolean isExistProductByStoreNo(@Param(value = "storeNo") int storeNo);

    void updateProductToDelete(@Param(value = "productNo") int productNo);

    void updateProductStock(@Param(value = "productNo") int productNo,
                            @Param(value = "stock") int stock,
                            @Param(value = "productStateEnum") ProductStateEnum productStateEnum);
}
