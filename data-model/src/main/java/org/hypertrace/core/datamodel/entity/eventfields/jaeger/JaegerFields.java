package org.hypertrace.core.datamodel.entity.eventfields.jaeger;

import java.util.List;

public class JaegerFields {

  private Integer flags;
  private List<String> logs;
  private List<String> warnings;

  public Integer getFlags() {
    return flags;
  }

  public List<String> getLogs() {
    return logs;
  }

  public List<String> getWarnings() {
    return warnings;
  }
}
