package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class EventRef {

  private ByteBuffer trace_id;
  private ByteBuffer event_id;
  private EventRefType ref_type;

  public ByteBuffer getTraceId() {
    return trace_id;
  }

  public ByteBuffer getEventId() {
    return event_id;
  }

  public EventRefType getRefType() {
    return ref_type;
  }
}
