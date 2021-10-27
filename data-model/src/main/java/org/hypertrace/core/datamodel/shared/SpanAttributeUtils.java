package org.hypertrace.core.datamodel.shared;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.avro.reflect.Nullable;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;

/**
 * Span being a very generic data structure with different attributes, reading attributes from Span
 * will be a common piece of code that will be written in different places and different modules
 * need to know the exact attribute name to be used. This utility class helps to reuse the code and
 * also make it easy not to worry about attribute names to pull out useful information from the
 * Span.
 */
public class SpanAttributeUtils {

  public static boolean isLeafSpan(StructuredTraceGraph structuredTraceGraph, Event event) {
    List<Event> childEvents = structuredTraceGraph.getChildrenEvents(event);
    return childEvents == null || childEvents.isEmpty();
  }

  public static String getStringAttributeWithDefault(
      Event event, String attributeKey, String defaultValue) {
    String value = getStringAttribute(event, attributeKey);
    return value == null ? defaultValue : value;
  }

  public static AttributeValue getAttributeValueWithDefault(
      Event event, String attributeKey, String defaultValue) {
    AttributeValue attributeValue = getAttributeValue(event, attributeKey);
    return attributeValue == null
        ? AttributeValue.newBuilder().setValue(defaultValue).build()
        : attributeValue;
  }

  public static AttributeValue getAttributeValue(Event event, String attributeKey) {
    if (event != null && event.getEnrichedAttributes() != null) {
      AttributeValue value = event.getEnrichedAttributes().getAttributeMap().get(attributeKey);
      if (value != null) {
        return value;
      }
    }
    if (event != null && event.getAttributes() != null) {
      return event.getAttributes().getAttributeMap().get(attributeKey);
    }
    return null;
  }

  public static AttributeValue getAttributeValue(Event.Builder eventBuilder, String attributeKey) {
    if (eventBuilder.getEnrichedAttributes() != null) {
      AttributeValue value =
          eventBuilder.getEnrichedAttributes().getAttributeMap().get(attributeKey);
      if (value != null) {
        return value;
      }
    }
    if (eventBuilder.getAttributes() != null) {
      return eventBuilder.getAttributes().getAttributeMap().get(attributeKey);
    }
    return null;
  }

  @Nullable
  public static String getStringAttribute(Event event, String attributeKey) {
    AttributeValue value = getAttributeValue(event, attributeKey);
    return value == null ? null : value.getValue();
  }

  public static Optional<String> getStringAttributeIgnoreKeyCase(Event event, String attributeKey) {
    if (event == null) {
      return Optional.empty();
    }
    if (event.getEnrichedAttributes() != null) {
      Optional<String> value =
          AttributeSearch.searchForAttributeIgnoreKeyCase(
              event.getEnrichedAttributes(), attributeKey);
      if (value.isPresent()) {
        return value;
      }
    }
    if (event.getAttributes() != null) {
      return AttributeSearch.searchForAttributeIgnoreKeyCase(event.getAttributes(), attributeKey);
    }
    return Optional.empty();
  }

  @Nullable
  public static String getStringAttribute(Event.Builder eventBuilder, String attributeKey) {
    AttributeValue value = getAttributeValue(eventBuilder, attributeKey);
    return value == null ? null : value.getValue();
  }

  public static boolean getBooleanAttribute(Event event, String attributeKey) {
    AttributeValue value = getAttributeValue(event, attributeKey);
    if (value == null) {
      return false;
    }

    return Boolean.parseBoolean(value.getValue());
  }

  /**
   * Utility method to get a single value from one of the given attributes. The first attribute
   * given in the list with non-null value will be used.
   */
  public static String getFirstAvailableStringAttribute(Event event, List<String> attributeKeys) {
    for (String key : attributeKeys) {
      String value = getStringAttribute(event, key);
      if (value != null && !value.isEmpty()) {
        return value;
      }
    }

    return null;
  }

  public static boolean containsAttributeKey(Event event, String attrKey) {
    // todo: need to fix that when creating event, the attributes and enriched attributes
    // shouldn't be null, so we can remove null-checks
    return containsAttributeKey(event.getAttributes(), attrKey)
        || containsAttributeKey(event.getEnrichedAttributes(), attrKey);
  }

  private static boolean containsAttributeKey(Attributes attributes, String attrKey) {
    return attributes != null
        && attributes.getAttributeMap() != null
        && attributes.getAttributeMap().containsKey(attrKey);
  }

  public static Map<String, AttributeValue> getAttributesWithPrefixKey(
      Event event, String attributeKeyPrefix) {
    if (event.getEnrichedAttributes() != null
        && event.getEnrichedAttributes().getAttributeMap() != null) {
      Map<String, AttributeValue> enrichedAttributeMap =
          event.getEnrichedAttributes().getAttributeMap();
      Map<String, AttributeValue> filteredEnrichedAttributeMap =
          enrichedAttributeMap.entrySet().stream()
              .filter(
                  entry ->
                      entry.getKey().toLowerCase().startsWith(attributeKeyPrefix.toLowerCase()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      if (!filteredEnrichedAttributeMap.isEmpty()) {
        return filteredEnrichedAttributeMap;
      }
    }

    if (event.getAttributes() != null && event.getAttributes().getAttributeMap() != null) {
      Map<String, AttributeValue> attributeMap = event.getAttributes().getAttributeMap();
      Map<String, AttributeValue> filteredAttributeMap =
          attributeMap.entrySet().stream()
              .filter(
                  entry ->
                      entry.getKey().toLowerCase().startsWith(attributeKeyPrefix.toLowerCase()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      if (!filteredAttributeMap.isEmpty()) {
        return filteredAttributeMap;
      }
    }
    return null;
  }

  @Nullable
  public static Event getParentSpan(
      Event span,
      Map<ByteBuffer, ByteBuffer> childToParentEventIds,
      Map<ByteBuffer, Event> idToEvent) {
    ByteBuffer parentSpanId = childToParentEventIds.get(span.getEventId());
    return parentSpanId != null ? idToEvent.get(parentSpanId) : null;
  }
}
