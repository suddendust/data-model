package org.hypertrace.core.datamodel.shared;

import org.hypertrace.core.datamodel.Metrics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AvroBuilderCacheTest {

  @Test
  public void testGetBuilderFromCache() {
    Metrics.Builder builder1 = AvroBuilderCache.fastNewBuilder(Metrics.Builder.class);
    Metrics.Builder builder2 = AvroBuilderCache.fastNewBuilder(Metrics.Builder.class);
    Assertions.assertNotSame(
        builder1, builder2, "Builders can't be same. Every builder has to be a new instance");
  }
}
