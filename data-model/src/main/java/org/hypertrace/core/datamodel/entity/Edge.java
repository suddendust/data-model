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
}
