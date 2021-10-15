package com.vsii.enamecard.anotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FullNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullName {

    String message() default "full name is incorrect!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
