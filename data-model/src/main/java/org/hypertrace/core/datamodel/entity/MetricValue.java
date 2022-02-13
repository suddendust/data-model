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

  public MetricValue setValue(Double value) {
    this.value = value;
    return this;
  }

  public MetricValue setBinaryValue(ByteBuffer binary_value) {
    this.binary_value = binary_value;
    return this;
  }

  public MetricValue setValueList(List<Double> value_list) {
    this.value_list = value_list;
    return this;
  }

  public MetricValue setValueMap(Map<String, Double> value_map) {
    this.value_map = value_map;
    return this;
  }
}
