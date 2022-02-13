package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class MetricValue {
  private Double value;
  private ByteBuffer binary_value;
  private List<Double> value_list;
  private Map<String, Double> value_map;

  public Double getValue() {
    return value;
  }

  public ByteBuffer getBinaryValue() {
    return binary_value;
  }

  public List<Double> getValueList() {
    return value_list;
  }

  public Map<String, Double> getValueMap() {
    return value_map;
  }
}
