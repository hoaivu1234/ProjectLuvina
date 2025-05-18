package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmployeeLoginIdValidator.class)
public @interface UniqueEmployeeLoginId {
    String message() default ErrorCodeConstants.ER003; // lỗi trùng mã
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

