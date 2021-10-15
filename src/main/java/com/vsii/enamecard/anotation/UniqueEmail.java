package com.vsii.enamecard.anotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
  String message() default "email is existed!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
