package org.hypertrace.core.datamodel.entity.eventfields.grpc;

import java.util.Map;

public class Response {

  private String body;
  private Integer size;
  private Integer status_code;
  private String status_message;
  private String error_name;
  private String error_message;
  private Map<String, String> metadata;
  private ResponseMetadata response_metadata;

  public String getBody() {
    return body;
  }

  public Integer getSize() {
    return size;
  }

  public Integer getStatusCode() {
    return status_code;
  }

  public String getStatusMessage() {
    return status_message;
  }

  public String getErrorName() {
    return error_name;
  }

  public String getErrorMessage() {
    return error_message;
  }

  public Map<String, String> getMetadata() {
    return metadata;
  }

  public ResponseMetadata getResponseMetadata() {
    return response_metadata;
  }
}
