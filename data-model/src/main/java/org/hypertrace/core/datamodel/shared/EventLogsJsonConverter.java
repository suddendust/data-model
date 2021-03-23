package org.hypertrace.core.datamodel.shared;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.EventLog;
import org.hypertrace.core.datamodel.EventLogs;

public class EventLogsJsonConverter {

  static String convertToJsonString(EventLogs eventLogs) {
    EventLogsJson eventLogsJson = convertToJson(eventLogs);
    return new Gson().toJson(eventLogsJson);
  }

  private static EventLogsJson convertToJson(EventLogs eventLogs) {
    return new EventLogsJson(eventLogs.getEventLogs()
        .stream()
        .map(EventLogsJsonConverter::convertToJson)
        .collect(Collectors.toList()));
  }

  private static EventLogJson convertToJson(EventLog eventLog) {
    return new EventLogJson(eventLog.getTimeStamp(), convertAttributes(eventLog.getAttributes()));
  }

  private static Map<String, String> convertAttributes(Attributes attributes) {
    Map<String, String> resultMap = new HashMap<>();
    if (attributes == null || attributes.getAttributeMap() == null) {
      return resultMap;
    }
    for (Map.Entry<String, AttributeValue> entry : attributes.getAttributeMap().entrySet()) {
      resultMap.put(entry.getKey(), entry.getValue().getValue());
    }
    return resultMap;
  }

  private static class EventLogsJson {
    List<EventLogJson> list;

    public EventLogsJson(List<EventLogJson> list) {
      this.list = list;
    }
  }

  private static class EventLogJson {
    long timeStamp;
    Map<String, String> records;

    public EventLogJson(
        long timeStamp,
        Map<String, String> records) {
      this.timeStamp = timeStamp;
      this.records = records;
    }
  }
}
