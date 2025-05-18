package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default ErrorCodeConstants.ER011;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


