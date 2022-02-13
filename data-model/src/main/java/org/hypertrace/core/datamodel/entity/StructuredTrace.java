package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;
import java.util.List;

public class StructuredTrace {

  private String customer_id;
  private ByteBuffer trace_id;
  private List<Entity> entity_list;
  private List<Resource> resource_list;
  private List<Event> event_list;
  private Attributes attributes;
  private Long start_time_millis;
  private Long end_time_millis;
  private Metrics metrics;
  private List<Edge> entity_edge_list;
  private List<Edge> event_edge_list;
  private List<Edge> entity_event_edge_list;
  private Graph entity_entity_graph;
  private Graph event_event_graph;
  private Graph entity_event_graph;
  private Timestamps timestamps;

  public String getCustomerId() {
    return customer_id;
  }

  public ByteBuffer getTraceId() {
    return trace_id;
  }

  public List<Entity> getEntityList() {
    return entity_list;
  }

  public List<Resource> getResourceList() {
    return resource_list;
  }

  public List<Event> getEventList() {
    return event_list;
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public Long getStartTimeMillis() {
    return start_time_millis;
  }

  public Long getEndTimeMillis() {
    return end_time_millis;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  public List<Edge> getEntityEdgeList() {
    return entity_edge_list;
  }

  public List<Edge> getEventEdgeList() {
    return event_edge_list;
  }

  public List<Edge> getEntityEventEdgeList() {
    return entity_event_edge_list;
  }

  public Graph getEntityEntityGraph() {
    return entity_entity_graph;
  }

  public Graph getEventEventGraph() {
    return event_event_graph;
  }

  public Graph getEntityEventGraph() {
    return entity_event_graph;
  }

  public Timestamps getTimestamps() {
    return timestamps;
  }
}
