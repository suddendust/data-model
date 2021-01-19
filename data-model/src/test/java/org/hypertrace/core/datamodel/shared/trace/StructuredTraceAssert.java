package org.hypertrace.core.datamodel.shared.trace;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

public class StructuredTraceAssert {

  public static void assertEntityEntityEdge(StructuredTrace structuredTrace) {
    Map<Integer, Entity> indexToEntityMap = new HashMap<>();
    List<Entity> entities = structuredTrace.getEntityList();

    int index = 0;
    for (Entity entity : entities) {
      indexToEntityMap.put(index++, entity);
    }

    List<Edge> entityEdges = structuredTrace.getEntityEdgeList();
    // source entity and target entity are different
    for (Edge edge : entityEdges) {
      Entity sourceEntity = indexToEntityMap.get(edge.getSrcIndex());
      Entity targetEntity = indexToEntityMap.get(edge.getTgtIndex());

      assertNotNull(sourceEntity);
      assertNotNull(targetEntity);
      assertNotEquals(sourceEntity, targetEntity);
    }
  }

  public static void assertEventEventEdge(StructuredTrace structuredTrace) {
    Map<Integer, Event> indexToEventMap = new HashMap<>();
    List<Event> events = structuredTrace.getEventList();

    int index = 0;
    for (Event event : events) {
      indexToEventMap.put(index++, event);
    }

    List<Edge> eventEdges = structuredTrace.getEventEdgeList();

    for (Edge edge : eventEdges) {
      Event srcEvent = indexToEventMap.get(edge.getSrcIndex());
      Event destEvent = indexToEventMap.get(edge.getTgtIndex());

      assertNotNull(srcEvent);
      assertNotNull(destEvent);
      assertNotEquals(srcEvent, destEvent);
    }
  }

  public static void assertEntityEventEdges(StructuredTrace structuredTrace) {
    Map<Integer, Event> indexToEventMap = new HashMap<>();
    List<Event> events = structuredTrace.getEventList();

    int index = 0;
    for (Event event : events) {
      indexToEventMap.put(index++, event);
    }

    Map<Integer, Entity> indexToEntityMap = new HashMap<>();
    List<Entity> entities = structuredTrace.getEntityList();

    index = 0;
    for (Entity entity : entities) {
      indexToEntityMap.put(index++, entity);
    }

    for (Edge edge : structuredTrace.getEntityEventEdgeList()) {
      Entity entity = indexToEntityMap.get(edge.getSrcIndex());
      Event event = indexToEventMap.get(edge.getTgtIndex());

      assertNotNull(entity);
      assertNotNull(event);
    }
  }
}
