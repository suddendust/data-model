package org.hypertrace.core.datamodel.shared.trace;

import org.hypertrace.core.datamodel.MetricValue;

public class MetricValueCreator {

  public static MetricValue create(double value) {
    return MetricValue.newBuilder().setValue(value).build();
  }

  public static MetricValue create(long value) {
    return MetricValue.newBuilder().setValue((double) value).build();
  }

  public static MetricValue create(int value) {
    return MetricValue.newBuilder().setValue((double) value).build();
  }
}
