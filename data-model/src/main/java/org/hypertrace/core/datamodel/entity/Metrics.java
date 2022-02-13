package org.hypertrace.core.datamodel.entity;

import java.util.Map;

public class Metrics {

  private Map<String, MetricValue> metric_map;

  public Metrics(
      Map<String, MetricValue> metric_map) {
    this.metric_map = metric_map;
  }

  public Map<String, MetricValue> getMetricMap() {
    return metric_map;
  }
}
