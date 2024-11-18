package com.kupstudio.bbarge.validation.validInterface.product;


import com.kupstudio.bbarge.validation.validator.product.ProductPriceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductPriceValidator.class)
public @interface ProductPriceValid {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}