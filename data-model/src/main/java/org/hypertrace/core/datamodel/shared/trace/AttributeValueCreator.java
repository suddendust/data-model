package org.hypertrace.core.datamodel.shared.trace;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hypertrace.core.datamodel.entity.AttributeValue;
import org.hypertrace.core.datamodel.shared.HexUtils;

public class AttributeValueCreator {

  public static AttributeValue create(String value) {
    return new AttributeValue().setValue(value);
  }

  public static AttributeValue create(boolean value) {
    return new AttributeValue().setValue(String.valueOf(value));
  }

  public static AttributeValue create(int value) {
    return new AttributeValue().setValue(String.valueOf(value));
  }

  public static AttributeValue create(long value) {
    return new AttributeValue().setValue(String.valueOf(value));
  }

  public static AttributeValue create(float value) {
    return new AttributeValue().setValue(String.valueOf(value));
  }

  public static AttributeValue create(double value) {
    return new AttributeValue().setValue(String.valueOf(value));
  }

  public static AttributeValue create(List<String> values) {
    return new AttributeValue().setValueList(values);
  }

  public static AttributeValue createFromByteBuffers(Set<ByteBuffer> values) {
    List<String> list = new ArrayList<>();
    values.forEach(
        value -> {
          list.add(new String(HexUtils.getBytes(value)));
        });
    return new AttributeValue().setValueList(list);
  }
}
