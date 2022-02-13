package org.hypertrace.core.datamodel.entity.eventfields.grpc;

import java.util.Map;

public class RequestMetadata {

  private String authority;
  private String content_type;
  private String path;
  private String x_forwarded_for;
  private String user_agent;
  private Map<String, String> other_metadata;

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

  public Map<String, String> getOtherMetadata() {
    return other_metadata;
  }
}
