package com.vsii.enamecard.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vsii.enamecard.model.response.Response;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.utils.HttpCodeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Arrays;
import java.util.Objects;

@ControllerAdvice
public class ExceptionAdvice {
  public static final String ERROR_MESSGE_FORMAT = "%s: %s";

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<SystemResponse> onConstraintValidationException(
    ConstraintViolationException e) {
    ConstraintViolation<?> firstViolation = e.getConstraintViolations().iterator().next();
    Path propertyPath = firstViolation.getPropertyPath();
    return new ResponseEntity<>(new SystemResponse(HttpCodeResponse.BAD_REQUEST, String.format(ERROR_MESSGE_FORMAT,
            propertyPath.toString().substring(propertyPath.toString().lastIndexOf(".") + 1),
            firstViolation.getMessage())),HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpErrorException.class)
  public ResponseEntity<SystemResponse<Object>> handleHttpException(
          HttpErrorException e) {
    return Response.httpError(e);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public <T> ResponseEntity<SystemResponse<T>> onMethodArgumentNotValidException(
          MethodArgumentNotValidException e) {
    FieldError firstError = e.getBindingResult().getFieldErrors().get(0);
    return Response.badRequest(400, Objects.requireNonNull(firstError.getDefaultMessage()));
  }

  @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public <T> ResponseEntity<SystemResponse<T>> handleInvalidFormatException(InvalidFormatException e) {
    String fieldName = e.getPath().get(0).getFieldName();
    Class<?> targetType = e.getTargetType();
    Object value = e.getValue();
    if(targetType.getEnumConstants() != null) {
      return Response.badRequest(400, String.format("%s: Invalid value '%s' of type %s, Accepted values: %s", fieldName, value, targetType.getSimpleName(),
              Arrays.toString(targetType.getEnumConstants())));
    }
    return Response.badRequest(400, String.format("%s: Invalid value '%s' of type %s", fieldName, value, targetType.getSimpleName()));
  }

}
