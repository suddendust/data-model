package org.hypertrace.core.datamodel.entity.eventfields.http;

import java.util.Map;

public class Request {

  private String url;
  private String scheme;
  private String host;
  private String method;
  private String path;
  private String query_string;
  private String body;
  private String session_id;
  private String cookies;
  private String user_agent;
  private Integer size;
  private RequestHeaders headers;
  private Map<String, String> params;
}
