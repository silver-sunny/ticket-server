package com.kupstudio.bbarge.validation.validInterface.store;


import com.kupstudio.bbarge.validation.validator.store.StoreNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StoreNameValidator.class)
public @interface StoreNameValid {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}