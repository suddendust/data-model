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
}
