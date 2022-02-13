package org.hypertrace.core.datamodel.entity;

import java.util.Map;

public class Timestamps {

  public Map<String, TimestampRecord> getRecords() {
    return records;
  }

  private Map<String, TimestampRecord> records;
}
