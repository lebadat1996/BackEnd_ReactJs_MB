package com.vsii.enamecard.anotation;

import com.vsii.enamecard.services.ENameCardService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private final ENameCardService eNameCardService;

  public UniqueEmailValidator(ENameCardService eNameCardService) {
    this.eNameCardService = eNameCardService;
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(email)){
      return true;
    }
    return !eNameCardService.existsByEmail(email);
  }

}
