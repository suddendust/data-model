package org.hypertrace.core.datamodel.shared;

import com.google.common.collect.ImmutableSet;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

/**
 * Builds Event graph, and Entity graph from Structured Trace This is a helper class to make it easy
 * for traversing the tree for Downstream Services
 */
public class StructuredTraceGraph {

  private final Map<ByteBuffer, Event> eventMap;
  private final Map<String, Entity> entityMap;

  /* there could be multiple roots for partial trace and incomplete instrumented app spans */
  private final Map<Event, List<Event>> parentToChildrenEvents;
  private final Map<Event, Event> childToParentEvents;
  /* entity have many-to-many relationship */
  private final Map<Entity, List<Entity>> parentToChildrenEntities;
  private final Map<Entity, List<Entity>> childToParentEntities;

  private Set<Event> rootEvents;
  private Set<Entity> rootEntities;

  private StructuredTraceGraph() {
    this.eventMap = new HashMap<>();
    this.entityMap = new HashMap<>();
    this.childToParentEntities = new HashMap<>();
    this.parentToChildrenEntities = new HashMap<>();
    this.childToParentEvents = new HashMap<>();
    this.parentToChildrenEvents = new HashMap<>();
  }

  /**
   * Create the instance with or without the full traversal graph includesFullGraph    Includes
   * computing the full traversal graph or not
   */
  public static StructuredTraceGraph createGraph(StructuredTrace trace) {
    StructuredTraceGraph graph = new StructuredTraceGraph();
    graph.buildParentChildRelationship(trace);
    graph.buildRootEvents(trace);
    graph.buildRootEntities(trace);
    return graph;
  }

  public Set<Event> getRootEvents() {
    return ImmutableSet.copyOf(rootEvents);
  }

  public Set<Entity> getRootEntities() {
    return ImmutableSet.copyOf(rootEntities);
  }

  public Event getParentEvent(Event event) {
    return childToParentEvents.get(event);
  }

  public List<Entity> getParentEntities(Entity entity) {
    return childToParentEntities.get(entity);
  }

  public List<Event> getChildrenEvents(Event event) {
    return parentToChildrenEvents.get(event);
  }

  public List<Entity> getChildrenEntities(Entity entity) {
    return parentToChildrenEntities.get(entity);
  }

  public Map<String, Entity> getEntityMap() {
    return entityMap;
  }

  private void buildParentChildRelationship(StructuredTrace trace) {
    List<Event> events = trace.getEventList();
    // there's no graph
    if (events == null) {
      return;
    }

    List<Entity> entities = trace.getEntityList();

    for (Event event : events) {
      eventMap.put(event.getEventId(), event);
    }

    List<Edge> eventEdges = trace.getEventEdgeList();
    if (eventEdges != null) {
      for (Edge edge : trace.getEventEdgeList()) {
        Integer sourceIndex = edge.getSrcIndex();
        Integer targetIndex = edge.getTgtIndex();
        Event parentEvent = events.get(sourceIndex);
        Event childEvent = events.get(targetIndex);
        parentToChildrenEvents.putIfAbsent(parentEvent, new ArrayList<>());
        parentToChildrenEvents.get(parentEvent).add(childEvent);
        childToParentEvents.put(childEvent, parentEvent);
      }
    }

    // return for now, we don't forsee this is to be null.
    if (entities == null) {
      return;
    }

    for (Entity entity : entities) {
      entityMap.put(entity.getEntityId(), entity);
    }

    List<Edge> entityEdges = trace.getEntityEdgeList();
    if (entityEdges != null) {
      for (Edge entityEdge : trace.getEntityEdgeList()) {
        Integer sourceIndex = entityEdge.getSrcIndex();
        Integer targetIndex = entityEdge.getTgtIndex();
        Entity parentEntity = entities.get(sourceIndex);
        Entity childEntity = entities.get(targetIndex);
        parentToChildrenEntities.putIfAbsent(parentEntity, new ArrayList<>());
        parentToChildrenEntities.get(parentEntity).add(childEntity);
        childToParentEntities.putIfAbsent(childEntity, new ArrayList<>());
        childToParentEntities.get(childEntity).add(parentEntity);
      }
    }
  }

  private void buildRootEvents(StructuredTrace trace) {
    // build the root entities
    rootEvents = new HashSet<>(trace.getEventList());
    // remove all the children, and what's remaining are the events without children
    // we will consider these are roots, including the ones that are standalone
    for (List<Event> childEvents : parentToChildrenEvents.values()) {
      rootEvents.removeAll(childEvents);
    }
  }

  private void buildRootEntities(StructuredTrace trace) {
    rootEntities = new HashSet<>(trace.getEntityList());
    for (List<Entity> childEntities : parentToChildrenEntities.values()) {
      rootEntities.removeAll(childEntities);
    }
  }


  public Map<ByteBuffer, Event> getEventMap() {
    return eventMap;
  }

  public Map<ByteBuffer, ByteBuffer> getChildIdsToParentIds() {
    return childToParentEvents.entrySet().stream()
        .collect(Collectors.toMap(
            e -> getEventId(e.getKey()),
            e -> getEventId(e.getValue())
        ));
  }

  public Map<ByteBuffer, List<ByteBuffer>> getParentToChildEventIds() {
    return parentToChildrenEvents.entrySet().stream()
        .collect(Collectors.toMap(
            e -> getEventId(e.getKey()),
            e -> e.getValue().stream().map(this::getEventId).collect(Collectors.toList()))
        );
  }

  private ByteBuffer getEventId(Event event) {
    if (event == null) {
      return null;
    }
    return event.getEventId();
  }
}
