package org.hypertrace.core.datamodel.entity;

public class TimestampRecord {

  public TimestampRecord(String name, Long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  private String name;
  private Long timestamp;

  public Long getTimestamp() {
    return timestamp;
  }

  public String getName() {
    return name;
  }
}
