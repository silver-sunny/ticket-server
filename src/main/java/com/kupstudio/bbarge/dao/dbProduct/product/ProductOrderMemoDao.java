package com.kupstudio.bbarge.dao.dbProduct.product;

import com.kupstudio.bbarge.dto.product.order.ProductOrderMemoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductOrderMemoDao {

    void insertMemo(ProductOrderMemoDto productOrderMemoDto);

    List<ProductOrderMemoDto> getMemoList(@Param(value = "orderNo") int orderNo);

    ProductOrderMemoDto getMemoDetail(@Param(value = "orderNo") int orderNo,
                                      @Param(value = "memoNo") int memoNo);

    void updateMemo(ProductOrderMemoDto productOrderMemoDto);

    void deleteMemo(@Param(value = "orderNo") int orderNo,
                    @Param(value = "memoNo") int memoNo);

}
