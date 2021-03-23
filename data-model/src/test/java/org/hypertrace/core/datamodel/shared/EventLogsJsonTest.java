package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.EventLog;
import org.hypertrace.core.datamodel.EventLogs;
import org.junit.jupiter.api.Test;

public class EventLogsJsonTest {

  @Test
  public void testConversion() {
    long t1 = 100, t2 = 150;
    EventLog eventLog1 = EventLog.newBuilder().setTimeStamp(t1).setAttributes(Attributes.newBuilder().setAttributeMap(
        Map.of("key1", AttributeValue.newBuilder().setValue("value1").build())).build()).build();

    EventLog eventLog2 = EventLog.newBuilder().setTimeStamp(t2).setAttributes(Attributes.newBuilder().setAttributeMap(
        Map.of("key1", AttributeValue.newBuilder().setValue("value1").build())).build()).build();

    EventLogs eventLogs = EventLogs.newBuilder().setEventLogs(List.of(eventLog1, eventLog2)).build();

    String jsonString = EventLogsJsonConverter.convertToJsonString(eventLogs);

    String expected = "{\"list\":[{\"timeStamp\":100,\"records\":{\"key1\":\"value1\"}},{\"timeStamp\":150,\"records\":{\"key1\":\"value1\"}}]}";
    assertEquals(expected, jsonString);
  }
}