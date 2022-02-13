package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class AttributeValue {
  private String value;
  private ByteBuffer binary_value;
  private List<String> value_list;
  private Map<String, String> value_map;
}
