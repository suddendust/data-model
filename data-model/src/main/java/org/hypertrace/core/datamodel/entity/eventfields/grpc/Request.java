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
}
