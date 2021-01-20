package org.hypertrace.core.datamodel.shared;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
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

  /* there could be multiple roots for partial trace and incomplete instrumented app spans */
  private final Map<ByteBuffer, List<Event>> parentToChildrenEvents;
  private final Map<ByteBuffer, Event> childToParentEvents;
  /* entity have many-to-many relationship */
  private final Map<String, List<Entity>> parentToChildrenEntities;
  private final Map<String, List<Entity>> childToParentEntities;

  /* these containers should be unmodifiable after initialization as we're exposing them via getters */
  private Map<ByteBuffer, Event> eventMap;
  private Map<String, Entity> entityMap;
  private Set<Event> rootEvents;
  private Set<Entity> rootEntities;

  private StructuredTraceGraph() {
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
    graph.buildEventMap(trace);
    graph.buildEntityMap(trace);
    graph.buildParentChildRelationship(trace);
    graph.buildRootEvents(trace);
    graph.buildRootEntities(trace);
    return graph;
  }

  /**
   * @return an immutable set containing the root events
   */
  public Set<Event> getRootEvents() {
    return rootEvents;
  }

  /**
   * @return an immutable set containing the root entities
   */
  public Set<Entity> getRootEntities() {
    return rootEntities;
  }

  public Event getParentEvent(Event event) {
    return childToParentEvents.get(event.getEventId());
  }

  public List<Entity> getParentEntities(Entity entity) {
    return childToParentEntities.get(entity.getEntityId());
  }

  public List<Event> getChildrenEvents(Event event) {
    return parentToChildrenEvents.get(event.getEventId());
  }

  public List<Entity> getChildrenEntities(Entity entity) {
    return parentToChildrenEntities.get(entity.getEntityId());
  }

  /**
   * @return an immutable map of entity ids to entities
   */
  public Map<String, Entity> getEntityMap() {
    return entityMap;
  }

  private void buildEventMap(StructuredTrace trace) {
    eventMap = trace.getEventList().stream()
        .collect(
            Collectors.toUnmodifiableMap(Event::getEventId, Function.identity(), (e1, e2) -> e2));

  }

  private void buildEntityMap(StructuredTrace trace) {
    entityMap = trace.getEntityList().stream()
        .collect(
            Collectors.toUnmodifiableMap(Entity::getEntityId, Function.identity(), (e1, e2) -> e2));
  }

  private void buildParentChildRelationship(StructuredTrace trace) {
    List<Event> events = trace.getEventList();
    // there's no graph
    if (events == null) {
      return;
    }

    List<Entity> entities = trace.getEntityList();
    List<Edge> eventEdges = trace.getEventEdgeList();
    if (eventEdges != null) {
      for (Edge edge : trace.getEventEdgeList()) {
        Integer sourceIndex = edge.getSrcIndex();
        Integer targetIndex = edge.getTgtIndex();
        Event parentEvent = events.get(sourceIndex);
        Event childEvent = events.get(targetIndex);
        parentToChildrenEvents.computeIfAbsent(parentEvent.getEventId(), k -> new ArrayList<>())
            .add(childEvent);
        childToParentEvents.put(childEvent.getEventId(), parentEvent);
      }
    }

    // return for now, we don't forsee this is to be null.
    if (entities == null) {
      return;
    }

    List<Edge> entityEdges = trace.getEntityEdgeList();
    if (entityEdges != null) {
      for (Edge entityEdge : trace.getEntityEdgeList()) {
        Integer sourceIndex = entityEdge.getSrcIndex();
        Integer targetIndex = entityEdge.getTgtIndex();
        Entity parentEntity = entities.get(sourceIndex);
        Entity childEntity = entities.get(targetIndex);
        parentToChildrenEntities.computeIfAbsent(parentEntity.getEntityId(), k -> new ArrayList<>())
            .add(childEntity);
        childToParentEntities.computeIfAbsent(childEntity.getEntityId(), k -> new ArrayList<>())
            .add(parentEntity);
      }
    }
  }

  private void buildRootEvents(StructuredTrace trace) {
    // build the root event ids
    Set<ByteBuffer> rootEventIds = trace.getEventList().stream().map(Event::getEventId)
        .collect(Collectors.toSet());

    // remove all the children, and what's remaining are the events without children
    // we will consider these are roots, including the ones that are standalone
    Set<ByteBuffer> childrenEventIds = parentToChildrenEvents.values().stream()
        .flatMap(l -> l.stream().map(Event::getEventId)).collect(Collectors.toSet());
    rootEventIds.removeAll(childrenEventIds);

    rootEvents = rootEventIds.stream().map(eventMap::get).collect(Collectors.toUnmodifiableSet());
  }

  private void buildRootEntities(StructuredTrace trace) {
    // build the root entity ids
    Set<String> rootEntityIds = trace.getEntityList().stream().map(Entity::getEntityId)
        .collect(Collectors.toSet());

    // remove all the children, and what's remaining are the entities without children
    // we will consider these are roots, including the ones that are standalone
    Set<String> childrenEntityIds = parentToChildrenEntities.values().stream()
        .flatMap(l -> l.stream().map(Entity::getEntityId)).collect(Collectors.toSet());
    rootEntityIds.removeAll(childrenEntityIds);

    rootEntities = rootEntityIds.stream().map(entityMap::get)
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * @return an immutable map of event ids to events
   */
  public Map<ByteBuffer, Event> getEventMap() {
    return eventMap;
  }

  public Map<ByteBuffer, ByteBuffer> getChildIdsToParentIds() {
    return childToParentEvents.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            e -> getEventId(e.getValue())
        ));
  }

  public Map<ByteBuffer, List<ByteBuffer>> getParentToChildEventIds() {
    return parentToChildrenEvents.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
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
