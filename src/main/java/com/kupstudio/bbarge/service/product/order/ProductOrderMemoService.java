package com.kupstudio.bbarge.service.product.order;

import com.kupstudio.bbarge.constant.product.ProductOrderConstant;
import com.kupstudio.bbarge.dao.dbProduct.product.ProductOrderMemoDao;
import com.kupstudio.bbarge.dto.product.order.ProductOrderMemoDto;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderMemoService {

    private final ProductOrderMemoDao productOrderMemoDao;


    public void insertMemo(ProductOrderMemoDto productOrderMemoDto) {
        productOrderMemoDao.insertMemo(productOrderMemoDto);
    }

    public List<ProductOrderMemoDto> getMemoList(int orderNo) {
        return productOrderMemoDao.getMemoList(orderNo);
    }

    public ProductOrderMemoDto getMemoDetail(int orderNo, int memoNo) {
        ProductOrderMemoDto productOrderMemoDto = productOrderMemoDao.getMemoDetail(orderNo, memoNo);

        if(ObjectUtils.isEmpty(productOrderMemoDto)){
            throw new PostNotExistException(ProductOrderConstant.MEMO_IS_NOT_EXIST);
        }

        return productOrderMemoDto;
    }

    public void updateMemo(ProductOrderMemoDto productOrderMemoDto) {
        productOrderMemoDao.updateMemo(productOrderMemoDto);
    }

    public void deleteMemo(int orderNo, int memoNo) {
        productOrderMemoDao.deleteMemo(orderNo, memoNo);
    }
}
