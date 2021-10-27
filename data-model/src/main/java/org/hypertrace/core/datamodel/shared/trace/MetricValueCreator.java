package org.hypertrace.core.datamodel.shared.trace;

import static org.hypertrace.core.datamodel.shared.AvroBuilderCache.fastNewBuilder;

import org.hypertrace.core.datamodel.MetricValue;

public class MetricValueCreator {

  public static MetricValue create(double value) {
    return fastNewBuilder(MetricValue.Builder.class).setValue(value).build();
  }

  public static MetricValue create(long value) {
    return fastNewBuilder(MetricValue.Builder.class).setValue((double) value).build();
  }

  public static MetricValue create(int value) {
    return fastNewBuilder(MetricValue.Builder.class).setValue((double) value).build();
  }
}
