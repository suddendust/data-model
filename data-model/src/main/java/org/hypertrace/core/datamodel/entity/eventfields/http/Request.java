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

  public String getUrl() {
    return url;
  }

  public String getScheme() {
    return scheme;
  }

  public String getHost() {
    return host;
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getQueryString() {
    return query_string;
  }

  public String getBody() {
    return body;
  }

  public String getSessionId() {
    return session_id;
  }

  public String getCookies() {
    return cookies;
  }

  public String getUserAgent() {
    return user_agent;
  }

  public Integer getSize() {
    return size;
  }

  public RequestHeaders getHeaders() {
    return headers;
  }

  public Map<String, String> getParams() {
    return params;
  }
}
