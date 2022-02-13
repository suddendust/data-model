package org.hypertrace.core.datamodel.entity;

import java.util.List;

public class ApiNodeEventEdge {

  private Integer src_event_index;
  private Integer tgt_event_index;
  private Integer src_api_node_index;
  private Integer tgt_api_node_index;
  private List<Attributes> attributes;
  private Long start_time_millis;
  private Long end_time_millis;
  private Metrics metrics;

  public Integer getSrcEventIndex() {
    return src_event_index;
  }

  public Integer getTgtEventIndex() {
    return tgt_event_index;
  }

  public Integer getSrcApiNodeIndex() {
    return src_api_node_index;
  }

  public Integer getTgtApiNodeIndex() {
    return tgt_api_node_index;
  }

  public List<Attributes> getAttributes() {
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
