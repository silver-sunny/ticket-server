package com.kupstudio.bbarge.exception.product;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class ProductExistException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public ProductExistException() {
        super(ErrorCodeEnum.PRODUCT_IS_EXIST.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.PRODUCT_IS_EXIST;
    }

    public ProductExistException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.PRODUCT_IS_EXIST;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
