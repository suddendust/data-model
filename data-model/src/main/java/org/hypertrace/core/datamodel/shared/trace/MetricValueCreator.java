package org.hypertrace.core.datamodel.shared.trace;

import org.hypertrace.core.datamodel.entity.MetricValue;

public class MetricValueCreator {

  public static MetricValue create(double value) {
    return new MetricValue().setValue(value);
  }

  public static MetricValue create(long value) {
    return new MetricValue().setValue((double) value);
  }

  public static MetricValue create(int value) {
    return new MetricValue().setValue((double) value);
  }
}
