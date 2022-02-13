package org.hypertrace.core.datamodel.entity.eventfields.http;

import java.util.List;

public class Response {
  private String body;
  private Integer status_code;
  private String status_message;
  private Integer size;
  private List<String> cookies;
  private ResponseHeaders headers;
}
