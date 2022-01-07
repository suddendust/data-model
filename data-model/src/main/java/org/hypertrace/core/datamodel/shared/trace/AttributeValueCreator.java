package org.hypertrace.core.datamodel.shared.trace;

import static org.hypertrace.core.datamodel.shared.AvroBuilderCache.fastNewBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.shared.HexUtils;

public class AttributeValueCreator {

  public static AttributeValue create(String value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(value).build();
  }

  public static AttributeValue create(boolean value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(int value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(long value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(float value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(double value) {
    return fastNewBuilder(AttributeValue.Builder.class).setValue(String.valueOf(value)).build();
  }

  public static AttributeValue create(List<String> values) {
    return fastNewBuilder(AttributeValue.Builder.class).setValueList(values).build();
  }

  public static AttributeValue createFromByteBuffers(Set<ByteBuffer> values) {
    List<String> list = new ArrayList<>();
    values.forEach(
        value -> {
          list.add(new String(HexUtils.getBytes(value)));
        });
    return fastNewBuilder(AttributeValue.Builder.class).setValueList(list).build();
  }
}
