package org.hypertrace.core.datamodel.entity.eventfields.http;

import java.util.List;

public class Response {
  private String body;
  private Integer status_code;
  private String status_message;
  private Integer size;
  private List<String> cookies;
  private ResponseHeaders headers;

  public String getBody() {
    return body;
  }

  public Integer getStatusCode() {
    return status_code;
  }

  public String getStatusMessage() {
    return status_message;
  }

  public Integer getSize() {
    return size;
  }

  public List<String> getCookies() {
    return cookies;
  }

  public ResponseHeaders getHeaders() {
    return headers;
  }
}
