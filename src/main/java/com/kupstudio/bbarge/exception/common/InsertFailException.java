package com.kupstudio.bbarge.exception.common;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class InsertFailException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;


    public InsertFailException() {
        super(ErrorCodeEnum.INSERT_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.INSERT_FAIL;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }
}
