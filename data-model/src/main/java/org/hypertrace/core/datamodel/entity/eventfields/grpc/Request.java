package org.hypertrace.core.datamodel.entity.eventfields.grpc;

import java.util.Map;

public class Request {

  private String method;
  private String host_port;
  private String call_options;
  private String body;
  private Integer size;
  private Map<String, String> metadata;
  private RequestMetadata request_metadata;

  public String getMethod() {
    return method;
  }

  public String getHostPort() {
    return host_port;
  }

  public String getCallOptions() {
    return call_options;
  }

  public String getBody() {
    return body;
  }

  public Integer getSize() {
    return size;
  }

  public Map<String, String> getMetadata() {
    return metadata;
  }

  public RequestMetadata getRequestMetadata() {
    return request_metadata;
  }
}
