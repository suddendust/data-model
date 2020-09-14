package org.hypertrace.core.datamodel.shared;

import java.nio.ByteBuffer;
import org.apache.commons.codec.binary.Hex;

public class HexUtils {

  public static String getHex(byte[] bytes) {
    return Hex.encodeHexString(bytes);
  }

  public static String getHex(ByteBuffer buffer) {
    return getHex(getBytes(buffer));
  }

  public static byte[] getBytes(ByteBuffer buffer) {
    // Mark the buffer so that we can reset it later.
    buffer.mark();
    try {
      // Set the position to '0' so that we can start reading from the beginning.
      buffer.position(0);
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      return bytes;
    } finally {
      // Always reset the mark.
      buffer.reset();
    }
  }
}
