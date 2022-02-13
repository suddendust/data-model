package org.hypertrace.core.datamodel.entity.eventfields.http;

import java.util.Map;

public class RequestHeaders {

  private String host;
  private String authority;
  private String content_type;
  private String path;
  private String x_forwarded_for;
  private String user_agent;
  private String cookie;
  private Map<String, String> other_headers;
}
