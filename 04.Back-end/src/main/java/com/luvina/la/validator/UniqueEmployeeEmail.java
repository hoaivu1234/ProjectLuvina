package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmployeeEmailValidator.class)
public @interface UniqueEmployeeEmail {
    String message() default ErrorCodeConstants.ER003;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
