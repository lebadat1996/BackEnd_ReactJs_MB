package com.vsii.enamecard.exceptions;

import org.springframework.http.HttpStatus;

public class HttpErrorException extends RuntimeException {
  private final HttpStatus status;
  private final String message;

  protected HttpErrorException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  private HttpErrorException(Exception e) {
    super(e);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.message = "Server error. Try again, please.";
  }

  public static HttpErrorException from(Exception e) {
    return new HttpErrorException(e);
  }

  public static HttpErrorException from(HttpStatus status, String message) {
    return new HttpErrorException(status, message);
  }

  public static HttpErrorException badRequest(String msg) {
    return from(HttpStatus.BAD_REQUEST, msg);
  }

  public static HttpErrorException conflict(String msg) {
    return from(HttpStatus.CONFLICT, msg);
  }

  public HttpStatus getStatus() {
    return status;
  }


  @Override
  public String getMessage() {
    return message;
  }

}
