package com.vsii.enamecard.anotation;

import com.vsii.enamecard.services.ENameCardService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone,String> {


    private final ENameCardService eNameCardService;

    public UniquePhoneValidator(ENameCardService eNameCardService) {
        this.eNameCardService = eNameCardService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)){
            return true;
        }

        return !eNameCardService.existsByPhone(value);
    }
}
