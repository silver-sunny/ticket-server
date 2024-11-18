package com.kupstudio.bbarge.exception.store;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;


public class StoreDeleteConditionFailException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public StoreDeleteConditionFailException() {
        super(ErrorCodeEnum.STORE_DELETE_CONDITION_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.STORE_DELETE_CONDITION_FAIL;
    }

    public StoreDeleteConditionFailException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.STORE_DELETE_CONDITION_FAIL;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
