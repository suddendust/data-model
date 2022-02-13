package org.hypertrace.core.datamodel.entity.eventfields.http;

import java.util.Map;

public class ResponseHeaders {

  private String content_type;
  private String set_cookie;
  private Map<String, String> other_headers;

  public String getContentType() {
    return content_type;
  }

  public String getSetCookie() {
    return set_cookie;
  }

  public Map<String, String> getOtherHeaders() {
    return other_headers;
  }
}
