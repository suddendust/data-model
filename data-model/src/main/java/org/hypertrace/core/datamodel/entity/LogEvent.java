package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class LogEvent {
  private Long timestamp_nanos;
  private String tenant_id;
  private ByteBuffer span_id;
  private ByteBuffer trace_id;
  private Attributes attributes;
}
