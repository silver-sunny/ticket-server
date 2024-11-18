package com.kupstudio.bbarge.validation.validator.product;


import com.kupstudio.bbarge.constant.product.ProductConstant;
import com.kupstudio.bbarge.validation.validInterface.product.ProductPriceValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductPriceValidator implements ConstraintValidator<ProductPriceValid, Integer> {

    @Override
    public void initialize(ProductPriceValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        // 가격이 null이 아니면서 일의 자리 값이 0이 아닐 경우
        if (value != null && value % 10 == 0) return true;

        addConstraintViolation(context, ProductConstant.PRICE_IS_NOT_VALID);

        return false;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg) {
        //기본 메시지 비활성화
        context.disableDefaultConstraintViolation();
        //새로운 메시지 추가
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}