package com.kupstudio.bbarge.exceptionService;

import com.kupstudio.bbarge.enumClass.error.ErrorCodeEnum;
import com.kupstudio.bbarge.exception.admin.AdminStoreExistException;
import com.kupstudio.bbarge.exception.admin.AdminStoreNotExistException;
import com.kupstudio.bbarge.exception.common.*;
import com.kupstudio.bbarge.exception.file.FileNotExistException;
import com.kupstudio.bbarge.exception.file.FileUrlException;
import com.kupstudio.bbarge.exception.product.NaverFailException;
import com.kupstudio.bbarge.exception.product.NotInChannelProductException;
import com.kupstudio.bbarge.exception.product.ProductExistException;
import com.kupstudio.bbarge.exception.store.StoreDeleteConditionFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLIntegrityConstraintViolationException;


@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity validException(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException", ex);
        return ApiResponseService.toResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("ConstraintViolationException", e);


        return ApiResponseService.toResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage());

    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private ResponseEntity sqlDuplicateException(SQLIntegrityConstraintViolationException e) {
        log.error("SQLIntegrityConstraintViolationException", e);


        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        log.error("HttpMediaTypeNotAcceptableException", e);

        return ApiResponseService.toResponseEntity(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    private ResponseEntity BindException(BindException e) {

        log.error("BindException", e);


        return ApiResponseService.toResponseEntity(ErrorCodeEnum.CONDITION_FAIL.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultipartException(MultipartException e) {

        log.error("MultipartException", e);

        return ApiResponseService.toResponseEntity(HttpStatus.BAD_REQUEST.value(), e.getMessage());

    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity httpClientErrorException(HttpClientErrorException e) {

        log.error("HttpClientErrorException", e);

        return ApiResponseService.toResponseEntity(e.getStatusCode().value(), e.getMessage());

    }
    
    @ExceptionHandler(ConditionFailException.class)
    protected ResponseEntity ConditionFailException(ConditionFailException e) {
        log.error("ConditionFailException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(InsertFailException.class)
    public ResponseEntity insertFailException(InsertFailException e) {

        log.error("InsertFailException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(DeleteFailException.class)
    public ResponseEntity deleteFailException(DeleteFailException e) {

        log.error("DeleteFailException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(PostNotExistException.class)
    private ResponseEntity postNotExistException(PostNotExistException e) {
        log.error("PostNotExistException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(WriterNotMatchException.class)
    private ResponseEntity writerNotMatchException(WriterNotMatchException e) {
        log.error("WriterNotMatchException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(FileUrlException.class)
    protected ResponseEntity fileUrlException(FileUrlException e) {
        log.error("FileUrlException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(FileNotExistException.class)
    protected ResponseEntity fileNotExistException(FileNotExistException e) {
        log.error("FileNotExistException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(NaverFailException.class)
    protected ResponseEntity naverFailException(NaverFailException e) {
        log.error("NaverFailException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }


    @ExceptionHandler(NotInChannelProductException.class)
    protected ResponseEntity notInChannelProductException(NotInChannelProductException e) {
        log.error("NotInChannelProductException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(ProductExistException.class)
    protected ResponseEntity productExistException(ProductExistException e) {
        log.error("ProductExistException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(AdminStoreExistException.class)
    protected ResponseEntity adminStoreExistException(AdminStoreExistException e) {
        log.error("AdminStoreExistException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(AdminStoreNotExistException.class)
    private ResponseEntity adminStoreNotExistException(AdminStoreNotExistException e) {
        log.error("AdminStoreNotExistException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(SmsNotValidException.class)
    private ResponseEntity smsNotValidException(SmsNotValidException e) {
        log.error("SmsNotValidException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

    @ExceptionHandler(StoreDeleteConditionFailException.class)
    private ResponseEntity storeDeleteConditionFailException(StoreDeleteConditionFailException e) {
        log.error("StoreDeleteConditionFailException", e);

        return ApiResponseService.toResponseEntity(e.getErrorCode(), e.getMessage());

    }

}
