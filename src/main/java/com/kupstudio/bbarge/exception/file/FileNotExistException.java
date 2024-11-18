package com.kupstudio.bbarge.exception.file;


import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;

public class FileNotExistException extends RuntimeException{

    private final ErrorCodeEnum errorCodeEnum;

    public FileNotExistException() {
        super(ErrorCodeEnum.FILE_RELATED_FAIL.getErrorMessage());
        this.errorCodeEnum = ErrorCodeEnum.FILE_RELATED_FAIL;
    }

    public FileNotExistException(String message){
        super(message);
        this.errorCodeEnum = ErrorCodeEnum.FILE_RELATED_FAIL;
    }

    public int getErrorCode(){
        return errorCodeEnum.getErrorCode();
    }

}
