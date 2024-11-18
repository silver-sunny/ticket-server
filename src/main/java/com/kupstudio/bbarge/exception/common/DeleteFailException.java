package com.kupstudio.bbarge.exception.common;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class DeleteFailException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;


    public DeleteFailException() {
        super(ErrorCodeEnum.DELETE_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.DELETE_FAIL;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }
}
