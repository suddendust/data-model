package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;

public class RawSpan {

  private String customer_id;
  private ByteBuffer trace_id;
  private Entity entity_list;
  private Resource resource;
  private Event event;
  private Long received_time_millis;

  public String getCustomerId() {
    return customer_id;
  }

  public ByteBuffer getTraceId() {
    return trace_id;
  }

  public Entity getEntityList() {
    return entity_list;
  }

  public Resource getResource() {
    return resource;
  }

  public Event getEvent() {
    return event;
  }

  public Long getReceivedTimeMillis() {
    return received_time_millis;
  }
}
