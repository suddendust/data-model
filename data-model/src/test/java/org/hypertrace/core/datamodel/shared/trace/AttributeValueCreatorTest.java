package org.hypertrace.core.datamodel.shared.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.List;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.entity.AttributeValue;
import org.junit.jupiter.api.Test;

class AttributeValueCreatorTest {
  @Test
  void testCreateFromByteBuffers() {
    String testString1 = "\u0001\u0002\u0003\u0004";
    String testString2 = "\u0005\u0006\u0007\u0008";

    ByteBuffer byteBuffer1 = ByteBuffer.wrap(testString1.getBytes());
    ByteBuffer byteBuffer2 = ByteBuffer.wrap(testString2.getBytes());

    LinkedHashSet<ByteBuffer> byteBufferSet = new LinkedHashSet<>();
    byteBufferSet.add(byteBuffer1);
    byteBufferSet.add(byteBuffer2);

    AttributeValue attributeValue = AttributeValueCreator.createFromByteBuffers(byteBufferSet);
    assertEquals(List.of(testString1, testString2), attributeValue.getValueList());

    // Calling AttributeValueCreator.createFromByteBuffers() should equal the same value from the
    // first call.
    AttributeValue attributeValue2 = AttributeValueCreator.createFromByteBuffers(byteBufferSet);
    assertEquals(List.of(testString1, testString2), attributeValue2.getValueList());
  }

  @Test
  void testCreateFromLong() {
    AttributeValue attributeValue = AttributeValueCreator.create(123L);
    assertEquals(123L, Long.valueOf(attributeValue.getValue()));
  }

  @Test
  void testCreateFromFloat() {
    AttributeValue attributeValue = AttributeValueCreator.create(12.34f);
    assertEquals(12.34f, Float.valueOf(attributeValue.getValue()));
  }

  @Test
  void testCreateFromDouble() {
    AttributeValue attributeValue = AttributeValueCreator.create(12.34d);
    assertEquals(12.34d, Double.valueOf(attributeValue.getValue()));
  }
}
