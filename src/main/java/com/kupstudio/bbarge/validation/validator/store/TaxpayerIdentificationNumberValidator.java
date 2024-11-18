package com.kupstudio.bbarge.validation.validator.store;


import com.kupstudio.bbarge.constant.store.StoreConstant;
import com.kupstudio.bbarge.validation.validInterface.store.TaxpayerIdentificationNumberValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaxpayerIdentificationNumberValidator implements ConstraintValidator<TaxpayerIdentificationNumberValid, String> {

    @Override
    public void initialize(TaxpayerIdentificationNumberValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            // null 값은 유효함
            return true;
        }

        if (value.trim().isEmpty()) {
            // 공백 값은 유효하지 않음
            addConstraintViolation(context, StoreConstant.CORPORATE_REGISTRATION_NUMBER_IS_EMPTY);
            return false;
        }

        // 사업자등록번호 정규식
        String pattern = "^[0-9-]+$";

        if(value.matches(pattern)){
            return true;
        } else {
            addConstraintViolation(context, StoreConstant.CORPORATE_REGISTRATION_NUMBER_NOT_VALID);
            return false;
        }

    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg) {
        //기본 메시지 비활성화
        context.disableDefaultConstraintViolation();
        //새로운 메시지 추가
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }

}