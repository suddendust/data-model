package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class TraceIdentity {
  private String tenant_id;
  private ByteBuffer trace_id;
}
