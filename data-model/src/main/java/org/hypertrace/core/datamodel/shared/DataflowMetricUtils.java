package org.hypertrace.core.datamodel.shared;

import io.micrometer.core.instrument.Timer;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.TimestampRecord;

import java.util.concurrent.TimeUnit;

/**
 * Utility methods to track flow of data and its lag across the services and data pipelines.
 */
public final class DataflowMetricUtils {

  /**
   * Compares current time against creation time of trace and reports that a lag via supplied timer.
   * @param trace whose with creation time.
   * @param timer which will be used to report the lag.
   */
  public static void reportArrivalLag(StructuredTrace trace, Timer timer) {
    if (trace.getTimestamps() != null && trace.getTimestamps().getRecords()
        .containsKey(DataflowMetric.CREATION_TIME.toString())) {
      timer.record(System.currentTimeMillis() -
              trace.getTimestamps().getRecords().get(DataflowMetric.CREATION_TIME.toString()).getTimestamp(),
          TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Inserts given {@link DataflowMetric} in the trace timeStamps record, with timestamp as current time.
   * If insert new TimestampRecord only when DataflowMetric.CREATION_TIME is already present.
   * @param trace in which timestamp record will be added.
   * @param metric against for which timestamp will be added.
   */
  public static void insertTimestamp(StructuredTrace trace, DataflowMetric metric) {
    if (trace.getTimestamps() != null && trace.getTimestamps().getRecords()
        .containsKey(DataflowMetric.CREATION_TIME.toString())) {
      trace.getTimestamps().getRecords().put(metric.toString(), new TimestampRecord(metric.toString(),
          System.currentTimeMillis()));
    }
  }

  /**
   * Wrapper to call reportArrivalLag and insertTimestamp.
   * @param trace trace
   * @param timer times
   * @param metric metric
   */
  public static void reportArrivalLagAndInsertTimestamp(StructuredTrace trace, Timer timer,
                                                        DataflowMetric metric) {
    reportArrivalLag(trace, timer);
    insertTimestamp(trace, metric);
  }
}
