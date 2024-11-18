package com.kupstudio.bbarge.exception.common;

import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class WriterNotMatchException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;


    public WriterNotMatchException() {
        super(ErrorCodeEnum.WRITER_NOT_MATCH.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.WRITER_NOT_MATCH;
    }

    public WriterNotMatchException(String message) {
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.WRITER_NOT_MATCH;
    }


    public int getErrorCode() {
        return errorCodeEnum.getErrorCode();
    }

}