package com.kupstudio.bbarge.exception.admin;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class AdminStoreNotExistException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public AdminStoreNotExistException() {
        super(ErrorCodeEnum.ADMIN_STORE_NOT_EXIST.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.ADMIN_STORE_NOT_EXIST;
    }

    public AdminStoreNotExistException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.ADMIN_STORE_NOT_EXIST;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
