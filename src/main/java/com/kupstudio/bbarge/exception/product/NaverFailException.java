package com.kupstudio.bbarge.exception.product;

import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class NaverFailException extends RuntimeException {


    private final ErrorCodeEnum errorCodeEnum;


    public NaverFailException() {
        super(ErrorCodeEnum.NAVER_FAIL_EXCEPTION.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.NAVER_FAIL_EXCEPTION;
    }

    public NaverFailException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.NAVER_FAIL_EXCEPTION;
    }


    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
