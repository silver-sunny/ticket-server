package com.kupstudio.bbarge.exception.common;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class PostNotExistException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public PostNotExistException() {
        super(ErrorCodeEnum.POST_NOT_EXIST.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.POST_NOT_EXIST;
    }

    public PostNotExistException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.POST_NOT_EXIST;
    }

    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}
