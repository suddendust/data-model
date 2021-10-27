package org.hypertrace.core.datamodel.shared;

import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.TimestampRecord;

/** Utility methods to track flow of data and its lag across the services and data pipelines. */
public final class DataflowMetricUtils {

  public static final String SPAN_ARRIVAL_TIME = "span.arrival.time";
  public static final String ARRIVAL_LAG = "arrival.lag";

  /**
   * Compares current time against span arrival time of trace and reports that a lag via supplied
   * timer.
   *
   * @param trace whose with creation time.
   * @param timer which will be used to report the lag.
   */
  public static void reportArrivalLag(StructuredTrace trace, Timer timer) {
    if (trace.getTimestamps() != null
        && trace.getTimestamps().getRecords().containsKey(SPAN_ARRIVAL_TIME)) {
      timer.record(
          System.currentTimeMillis()
              - trace.getTimestamps().getRecords().get(SPAN_ARRIVAL_TIME).getTimestamp(),
          TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Inserts given metric in the trace timeStamps record, with timestamp as current time. If insert
   * new TimestampRecord only when SPAN_ARRIVAL_TIME is already present.
   *
   * @param trace in which timestamp record will be added.
   * @param metricName against for which timestamp will be added.
   */
  public static void insertTimestamp(StructuredTrace trace, String metricName) {
    if (trace.getTimestamps() != null
        && trace.getTimestamps().getRecords().containsKey(SPAN_ARRIVAL_TIME)) {
      trace
          .getTimestamps()
          .getRecords()
          .put(metricName, new TimestampRecord(metricName, System.currentTimeMillis()));
    }
  }

  /**
   * Wrapper to call reportArrivalLag and insertTimestamp.
   *
   * @param trace trace
   * @param timer times
   * @param metricName metric
   */
  public static void reportArrivalLagAndInsertTimestamp(
      StructuredTrace trace, Timer timer, String metricName) {
    reportArrivalLag(trace, timer);
    insertTimestamp(trace, metricName);
  }
}
