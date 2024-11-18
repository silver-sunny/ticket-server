package com.kupstudio.bbarge.validation.validator.store;


import com.kupstudio.bbarge.constant.store.StoreConstant;
import com.kupstudio.bbarge.validation.validInterface.store.StoreNameValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StoreNameValidator implements ConstraintValidator<StoreNameValid, String> {

    @Override
    public void initialize(StoreNameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            // 빈 값은 유효하지 않음
            addConstraintViolation(context, StoreConstant.STORE_NAME_IS_EMPTY);
            return false;
        }

        // 영문, 한글만 사용 가능 (모음 자음 불가)
        String pattern = "^[a-zA-Z0-9가-힣]*$";

        if (value.matches(pattern)) {
            return true;
        } else {
            addConstraintViolation(context, StoreConstant.STORE_NAME_NOT_VALID);
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