package org.hypertrace.core.datamodel.shared.trace;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hypertrace.core.datamodel.AttributeValue;

public class AttributeValueCreator {

  public static AttributeValue create(String value) {
    return AttributeValue.newBuilder().setValue(value).build();
  }

  public static AttributeValue create(boolean value) {
    return AttributeValue.newBuilder().setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(int value) {
    return AttributeValue.newBuilder().setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(List<String> values) {
    return AttributeValue.newBuilder().setValueList(values).build();
  }

  public static AttributeValue createFromByteBuffers(Set<ByteBuffer> values) {
    List<String> list = new ArrayList<>();
    values.forEach(value -> {
      byte[] buf = new byte[value.remaining()];
      value.get(buf);
      list.add(new String(buf));
    });
    return AttributeValue.newBuilder().setValueList(list).build();
  }
}
