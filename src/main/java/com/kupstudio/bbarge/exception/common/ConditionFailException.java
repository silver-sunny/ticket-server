package com.kupstudio.bbarge.exception.common;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;


public class ConditionFailException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public ConditionFailException() {
        super(ErrorCodeEnum.CONDITION_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.CONDITION_FAIL;
    }

    public ConditionFailException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.CONDITION_FAIL;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
