package com.kupstudio.bbarge.validation.validInterface.store;


import com.kupstudio.bbarge.validation.validator.store.TaxpayerIdentificationNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TaxpayerIdentificationNumberValidator.class})
public @interface TaxpayerIdentificationNumberValid {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}