package org.hypertrace.core.datamodel.entity;

import java.util.Map;

public class Metrics {
  private Map<String, MetricValue> metric_map;

  public Map<String, MetricValue> getMetricMap() {
    return metric_map;
  }
}
