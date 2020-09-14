package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class HexUtilsTest {
  @Test
  public void testGetHex() {
    String testString = "\u0001\u0002\u0003\u0004";
    ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

    assertEquals("01020304", HexUtils.getHex(byteBuffer));
    // The first call should make sure to reset the position of the buffer to 0. We should get back
    // a value equal to the first call if we make the call again.
    assertEquals("01020304", HexUtils.getHex(byteBuffer));
  }

  @Test
  public void testGetBytes() {
    String testString = "\u0001\u0002\u0003\u0004";
    ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

    byte[] expectedBytes = new byte[] {1, 2, 3, 4};
    compareByteArrays(expectedBytes, HexUtils.getBytes(byteBuffer));
    // The first call should make sure to reset the position of the buffer to 0. We should get back
    // a value equal to the first call if we make the call again.
    compareByteArrays(expectedBytes, HexUtils.getBytes(byteBuffer));
  }

  private void compareByteArrays(byte[] expectedBytes, byte[] actualBytes) {
    for (int i = 0; i < expectedBytes.length; i++) {
      assertEquals(expectedBytes[i], actualBytes[i]);
    }
  }
}
