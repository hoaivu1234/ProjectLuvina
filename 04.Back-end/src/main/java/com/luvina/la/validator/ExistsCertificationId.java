package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsCertificationIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsCertificationId {
    String message() default ErrorCodeConstants.ER004;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
