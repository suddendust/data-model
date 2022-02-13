package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class LogEvent {
  private Long timestamp_nanos;
  private String tenant_id;
  private ByteBuffer span_id;
  private ByteBuffer trace_id;
  private Attributes attributes;

  public Long getTimestampNanos() {
    return timestamp_nanos;
  }

  public String getTenantId() {
    return tenant_id;
  }

  public ByteBuffer getSpanId() {
    return span_id;
  }

  public ByteBuffer getTraceId() {
    return trace_id;
  }

  public Attributes getAttributes() {
    return attributes;
  }
}
