package com.aexp.acq.go2.base;

public class RestResponse {

  public String message;
  public String errorCode;
  public Integer status;
  public long time;
 
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getResponseBody() {
    String s = message;
    if (s == null) {
      s = "";
    }
    return s;
  }

}
