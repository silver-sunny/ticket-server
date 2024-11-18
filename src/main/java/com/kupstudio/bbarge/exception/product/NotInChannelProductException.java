package com.kupstudio.bbarge.exception.product;

import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class NotInChannelProductException extends RuntimeException {


    private final ErrorCodeEnum errorCodeEnum;


    public NotInChannelProductException() {
        super(ErrorCodeEnum.NOT_IN_CHANNEL_PRODUCT.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.NOT_IN_CHANNEL_PRODUCT;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
