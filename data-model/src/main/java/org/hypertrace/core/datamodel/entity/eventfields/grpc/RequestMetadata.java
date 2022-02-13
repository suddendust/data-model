package org.hypertrace.core.datamodel.entity.eventfields.grpc;

import java.util.Map;

public class RequestMetadata {

  private String authority;
  private String content_type;
  private String path;
  private String x_forwarded_for;
  private String user_agent;
  private Map<String, String> other_metadata;
}
