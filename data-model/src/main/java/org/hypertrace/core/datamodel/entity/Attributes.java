package org.hypertrace.core.datamodel.entity;

import java.util.Map;

public class Attributes {

  private Map<String, AttributeValue> attribute_map;

  public Attributes(Map<String, AttributeValue> attribute_map) {
    this.attribute_map = attribute_map;
  }

  public Map<String, AttributeValue> getAttributeMap() {
    return attribute_map;
  }
}
