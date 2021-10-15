package com.vsii.enamecard.model.response;

public class SystemResponse<T> {

  private int status;

  private String error;

  private T data;

  public SystemResponse() {
  }

  public SystemResponse(int status, String message) {
    this.status = status;
    this.error = message;
  }

  public SystemResponse(int status, String message, T data) {
    this.status = status;
    this.error = message;
    this.data = data;
  }

  public int getStatus() {
    return status;
  }

  public SystemResponse setStatus(int status) {
    this.status = status;
    return this;
  }

  public String getError() {
    return error;
  }

  public SystemResponse setError(String error) {
    this.error = error;
    return this;
  }

  public T getData() {
    return data;
  }

  public SystemResponse setData(T data) {
    this.data = data;
    return this;
  }
}
