package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class AttributeValue {
  private String value;
  private ByteBuffer binary_value;
  private List<String> value_list;
  private Map<String, String> value_map;

  public String getValue() {
    return value;
  }

  public ByteBuffer getBinaryValue() {
    return binary_value;
  }

  public List<String> getValueList() {
    return value_list;
  }

  public Map<String, String> getValueMap() {
    return value_map;
  }

  public AttributeValue setValue(String value) {
    this.value = value;
    return this;
  }

  public AttributeValue setBinaryValue(ByteBuffer binary_value) {
    this.binary_value = binary_value;
    return this;
  }

  public AttributeValue setValueList(List<String> value_list) {
    this.value_list = value_list;
    return this;
  }

  public AttributeValue setValueMap(Map<String, String> value_map) {
    this.value_map = value_map;
    return this;
  }
}
