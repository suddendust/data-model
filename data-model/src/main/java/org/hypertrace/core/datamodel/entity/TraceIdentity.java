package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class TraceIdentity {
  private String tenant_id;

  public String getTenantId() {
    return tenant_id;
  }

  public ByteBuffer getTraceId() {
    return trace_id;
  }

  private ByteBuffer trace_id;
}
