package org.hypertrace.core.datamodel.entity;

public class Edge {

  private EdgeType edge_type;
  private Integer src_index;
  private Integer tgt_index;
  private Attributes attributes;
  private Long start_time_millis;
  private Long end_time_millis;
  private Metrics metrics;

  public EdgeType getEdgeType() {
    return edge_type;
  }

  public Integer getSrcIndex() {
    return src_index;
  }

  public Integer getTgtIndex() {
    return tgt_index;
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

  public Edge setEdgeType(EdgeType edge_type) {
    this.edge_type = edge_type;
    return this;
  }

  public Edge setSrcIndex(Integer src_index) {
    this.src_index = src_index;
    return this;
  }

  public Edge setTgtIndex(Integer tgt_index) {
    this.tgt_index = tgt_index;
    return this;
  }

  public Edge setAttributes(Attributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public Edge setStartTimeMillis(Long start_time_millis) {
    this.start_time_millis = start_time_millis;
    return this;
  }

  public Edge setEndTimeMillis(Long end_time_millis) {
    this.end_time_millis = end_time_millis;
    return this;
  }

  public Edge setMetrics(Metrics metrics) {
    this.metrics = metrics;
    return this;
  }
}
