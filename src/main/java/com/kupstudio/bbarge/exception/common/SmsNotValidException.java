package com.kupstudio.bbarge.exception.common;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class SmsNotValidException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public SmsNotValidException() {
        super(ErrorCodeEnum.SMS_TO_NOT_EXIST.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.SMS_TO_NOT_EXIST;
    }

    public SmsNotValidException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.SMS_TO_NOT_EXIST;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
