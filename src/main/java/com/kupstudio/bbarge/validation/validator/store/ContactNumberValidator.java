package com.kupstudio.bbarge.validation.validator.store;


import com.kupstudio.bbarge.constant.store.StoreConstant;
import com.kupstudio.bbarge.validation.validInterface.store.ContactNumberValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberValid, String> {

    @Override
    public void initialize(ContactNumberValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            addConstraintViolation(context, StoreConstant.STORE_CONTACT_NUMBER_IS_EMPTY);
            return false;
        }

        String pattern = "^\\d{2,3}[-]+\\d{3,4}[-]+\\d{4}$";

        if(value.matches(pattern)){
            return true;
        } else {
            addConstraintViolation(context, StoreConstant.STORE_CONTACT_NUMBER_NOT_VALID);
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