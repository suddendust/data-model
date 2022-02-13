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

  public StructuredTrace setCustomerId(String customer_id) {
    this.customer_id = customer_id;
    return this;
  }

  public StructuredTrace setTraceId(ByteBuffer trace_id) {
    this.trace_id = trace_id;
    return this;
  }

  public StructuredTrace setEntityList(
      List<Entity> entity_list) {
    this.entity_list = entity_list;
    return this;
  }

  public StructuredTrace setResourceList(
      List<Resource> resource_list) {
    this.resource_list = resource_list;
    return this;
  }

  public StructuredTrace setEventList(
      List<Event> event_list) {
    this.event_list = event_list;
    return this;
  }

  public StructuredTrace setAttributes(Attributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public StructuredTrace setStartTimeMillis(Long start_time_millis) {
    this.start_time_millis = start_time_millis;
    return this;
  }

  public StructuredTrace setEndTimeMillis(Long end_time_millis) {
    this.end_time_millis = end_time_millis;
    return this;
  }

  public StructuredTrace setMetrics(Metrics metrics) {
    this.metrics = metrics;
    return this;
  }

  public StructuredTrace setEntityEdgeList(
      List<Edge> entity_edge_list) {
    this.entity_edge_list = entity_edge_list;
    return this;
  }

  public StructuredTrace setEventEdgeList(
      List<Edge> event_edge_list) {
    this.event_edge_list = event_edge_list;
    return this;
  }

  public StructuredTrace setEntityEventEdgeList(
      List<Edge> entity_event_edge_list) {
    this.entity_event_edge_list = entity_event_edge_list;
    return this;
  }

  public StructuredTrace setEntityEntityGraph(
      Graph entity_entity_graph) {
    this.entity_entity_graph = entity_entity_graph;
    return this;
  }

  public StructuredTrace setEventEventGraph(
      Graph event_event_graph) {
    this.event_event_graph = event_event_graph;
    return this;
  }

  public StructuredTrace setEntityEventGraph(
      Graph entity_event_graph) {
    this.entity_event_graph = entity_event_graph;
    return this;
  }

  public StructuredTrace setTimestamps(Timestamps timestamps) {
    this.timestamps = timestamps;
    return this;
  }
}
