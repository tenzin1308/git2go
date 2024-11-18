package com.aexp.acq.go2.base;

public class GraphResponse {

  private String response;
  private int statusCode;
  private String message;
  private long time;

  /**
   * This method returns the response time in milliseconds.
   *
   * @return long
   */
  public long getTime() {
    return time;
  }

  /**
   * This method sets the response time in milliseconds.
   *
   * @param time
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * This method returns the HTTP response message associated with the status code.
   *
   * @return String
   */
  public String getMessage() {
    return message;
  }

  /**
   * This method sets the HTTP response message associated with the status code.
   *
   * @param message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * This method returns the HTTP status code.
   *
   * @return int
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * This method sets the HTTP status code.
   *
   * @param statusCode
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * This method returns the actual content of the response.
   *
   * @return String
   */
  public String getResponse() {
    return response;
  }

  /**
   * This method sets the actual content of the response.
   *
   * @param response
   */
  public void setResponse(String response) {
    this.response = response;
  }

}
