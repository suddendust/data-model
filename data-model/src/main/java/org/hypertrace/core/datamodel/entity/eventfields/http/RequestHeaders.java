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

  public String getHost() {
    return host;
  }

  public String getAuthority() {
    return authority;
  }

  public String getContentType() {
    return content_type;
  }

  public String getPath() {
    return path;
  }

  public String getXForwardedFor() {
    return x_forwarded_for;
  }

  public String getUserAgent() {
    return user_agent;
  }

  public String getCookie() {
    return cookie;
  }

  public Map<String, String> getOtherHeaders() {
    return other_headers;
  }
}
