package com.kupstudio.bbarge.exception.file;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class FileUrlException extends RuntimeException{

    private final ErrorCodeEnum errorCodeEnum;

    public FileUrlException() {
        super(ErrorCodeEnum.FILE_RELATED_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.FILE_RELATED_FAIL;
    }

    public FileUrlException(String message){
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.FILE_RELATED_FAIL;
    }

    public int getErrorCode(){
        return errorCodeEnum.getErrorCode();
    }


}
