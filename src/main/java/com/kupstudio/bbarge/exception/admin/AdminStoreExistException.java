package com.kupstudio.bbarge.exception.admin;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class AdminStoreExistException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public AdminStoreExistException() {
        super(ErrorCodeEnum.ADMIN_STORE_IS_EXIST.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.ADMIN_STORE_IS_EXIST;
    }

    public AdminStoreExistException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.ADMIN_STORE_IS_EXIST;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
