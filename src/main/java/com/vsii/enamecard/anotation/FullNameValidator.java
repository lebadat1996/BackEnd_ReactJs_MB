package com.vsii.enamecard.anotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullNameValidator implements ConstraintValidator<FullName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)){
            return false;
        }

        return checkFormatFullName(value);
    }

    public boolean checkFormatFullName(String value) {
        Pattern pattern = Pattern.compile("[`!@#$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>\\/?~0-9]");
        Matcher matcher = pattern.matcher(value);
        return !matcher.find();
    }
}
