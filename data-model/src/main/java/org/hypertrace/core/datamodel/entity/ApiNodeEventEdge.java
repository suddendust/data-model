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
}
