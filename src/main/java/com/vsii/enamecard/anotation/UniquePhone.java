package com.vsii.enamecard.anotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneValidator.class)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhone {

    String message() default "number phone is existed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
