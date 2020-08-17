package org.hypertrace.core.datamodel.shared;

import io.micrometer.core.instrument.Timer;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.TimestampRecord;
import org.hypertrace.core.datamodel.Timestamps;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataFlowMetricUtilTest {

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
    recordMap.put(DataFlowMetrics.CREATION_TIME.toString(), record);
    DataFlowMetricUtil.reportArrivalLag(trace, timer);
    verify(timer, times(1)).record(anyLong(), any(TimeUnit.class));
  }

  @Test
  public void test_insertTimestamp_null() {
    StructuredTrace trace = new StructuredTrace();
    DataFlowMetricUtil.insertTimestamp(trace, DataFlowMetrics.CREATION_TIME);
    assertNotNull(trace.getTimestamps());
    assertTrue(trace.getTimestamps().getRecords().containsKey(DataFlowMetrics.CREATION_TIME.toString()));
  }

  @Test
  public void test_insertTimestamp_notNull() {
    when(trace.getTimestamps()).thenReturn(timestamps);
    when(timestamps.getRecords()).thenReturn(recordMap);
    DataFlowMetricUtil.insertTimestamp(trace, DataFlowMetrics.ENRICHMENT_ARRIVAL_LAG);
    assertTrue(trace.getTimestamps().getRecords().containsKey(DataFlowMetrics.ENRICHMENT_ARRIVAL_LAG.toString()));
  }
}