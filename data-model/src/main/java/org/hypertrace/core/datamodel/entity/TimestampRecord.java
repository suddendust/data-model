package org.hypertrace.core.datamodel.entity;

public class TimestampRecord {

  public String getName() {
    return name;
  }

  public TimestampRecord(String name, Long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  private String name;
  private Long timestamp;
}
