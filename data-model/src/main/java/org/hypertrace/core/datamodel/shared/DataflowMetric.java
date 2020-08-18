package org.hypertrace.core.datamodel.shared;

/**
 * Set of metrics that can be used to track the flow of data from and its processing times as it passes through various
 * components.
 */
public enum DataflowMetric {
  CREATION_TIME("creation.time"),
  ENRICHMENT_ARRIVAL_TIME("enrichment.arrival.time"),
  VIEW_GENERATION_ARRIVAL_TIME("view.generation.arrival.time");

  public final String metric;
  DataflowMetric(String metric) {
    this.metric = metric;
  }

  @Override
  public String toString() {
    return this.metric;
  }
}
