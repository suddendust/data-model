package org.hypertrace.core.datamodel.shared;

import io.micrometer.core.instrument.Timer;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.TimestampRecord;
import org.hypertrace.core.datamodel.Timestamps;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataflowMetricUtilsTest {

  StructuredTrace trace = mock(StructuredTrace.class);
  Timer timer = mock(Timer.class);
  Timestamps timestamps = mock(Timestamps.class);
  Map<String, TimestampRecord> recordMap = new HashMap<>();
  TimestampRecord record = mock(TimestampRecord.class);

  @Test
  public void test_reportArrivalLag() {
    when(trace.getTimestamps()).thenReturn(timestamps);
    when(timestamps.getRecords()).thenReturn(recordMap);
    when(record.getTimestamp()).thenReturn(System.currentTimeMillis());
    recordMap.put(DataflowMetric.CREATION_TIME.toString(), record);
    DataflowMetricUtils.reportArrivalLag(trace, timer);
    verify(timer, times(1)).record(anyLong(), any(TimeUnit.class));
  }

  @Test
  public void test_insertTimestamp() {
    when(trace.getTimestamps()).thenReturn(timestamps);
    when(timestamps.getRecords()).thenReturn(recordMap);
    when(record.getTimestamp()).thenReturn(System.currentTimeMillis());
    recordMap.put(DataflowMetric.CREATION_TIME.toString(), record);
    DataflowMetricUtils.insertTimestamp(trace, DataflowMetric.ENRICHMENT_ARRIVAL_TIME);
    assertTrue(trace.getTimestamps().getRecords().containsKey(DataflowMetric.ENRICHMENT_ARRIVAL_TIME.toString()));
  }
}