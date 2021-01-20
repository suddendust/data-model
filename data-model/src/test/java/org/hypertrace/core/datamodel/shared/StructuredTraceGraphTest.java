package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.EventRef;
import org.hypertrace.core.datamodel.EventRefType;
import org.hypertrace.core.datamodel.Metrics;
import org.hypertrace.core.datamodel.RawSpan;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.shared.trace.StructuredTraceAssert;
import org.hypertrace.core.datamodel.shared.trace.StructuredTraceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StructuredTraceGraphTest {

  private static final String CUSTOMER_ID = "customer_id";

  @Mock
  private StructuredTrace trace;
  private List<Event> events;
  private List<Entity> entities;
  private List<Edge> eventEdges;
  private List<Edge> entityEdges;
  private StructuredTraceGraph graph;
  private Map<ByteBuffer, Event> expectedEventMap;
  private Map<ByteBuffer, ByteBuffer> childToParentIds;

  @BeforeEach
  void setup() {
    /* to reset the mocks for each @Test */
    MockitoAnnotations.initMocks(this);
    events = new ArrayList<>();
    entities = new ArrayList<>();
    eventEdges = new ArrayList<>();
    entityEdges = new ArrayList<>();
    expectedEventMap = new HashMap<>();
    childToParentIds = new HashMap<>();
    setupEventAndEntityMocks(4);
  }

  @Test
  void test_createGraph_withValidInput() {
    createGraph_shouldCreateCorrectGraph();
  }

  private void createGraph_shouldCreateCorrectGraph() {
    int rootIndex1 = 0;
    int rootIndex2 = 1;
    int sourceIdx1 = rootIndex1;
    int targetIdx1 = 2;
    int targetIdx2 = 3;
    addEdges(sourceIdx1, targetIdx1, eventEdges);
    addEdges(sourceIdx1, targetIdx1, entityEdges);
    addEdges(targetIdx1, targetIdx2, eventEdges);
    addEdges(targetIdx1, targetIdx2, entityEdges);
    childToParentIds.put(events.get(targetIdx1).getEventId(), events.get(sourceIdx1).getEventId());
    childToParentIds.put(events.get(targetIdx2).getEventId(), events.get(targetIdx1).getEventId());

    when(trace.getEventEdgeList()).thenReturn(eventEdges);
    when(trace.getEntityEdgeList()).thenReturn(entityEdges);

    Set<Event> expectedRootEvents = Sets.newHashSet(events.get(rootIndex1), events.get(rootIndex2));
    Set<Entity> expectedRootEntities =
        Sets.newHashSet(entities.get(rootIndex1), entities.get(rootIndex2));

    graph = StructuredTraceGraph.createGraph(trace);
    assertEquals(expectedRootEntities, graph.getRootEntities());
    assertEquals(expectedRootEvents, graph.getRootEvents());
    assertEquals(events.get(sourceIdx1), graph.getParentEvent(events.get(targetIdx1)));
    assertEquals(Lists.newArrayList(entities.get(sourceIdx1)),
        graph.getParentEntities(entities.get(targetIdx1)));
    List<Entity> root1Children = graph.getChildrenEntities(entities.get(rootIndex1));
    assertEquals(Lists.newArrayList(entities.get(targetIdx1)), root1Children);
    assertEquals(expectedEventMap, graph.getEventMap());
    assertEquals(childToParentIds, graph.getChildIdsToParentIds());
  }

  private void setupEventAndEntityMocks(int totalEvent) {
    for (int index = 0; index < totalEvent; index++) {
      Event event = mock(Event.class);
      when(event.getEventId())
          .thenReturn(ByteBuffer.wrap(String.valueOf(index).getBytes()));
      events.add(event);

      expectedEventMap.put(event.getEventId(), event);
      Entity entity = mock(Entity.class);
      when(entity.getEntityId())
          .thenReturn(String.valueOf(index));
      entities.add(entity);
    }
    when(trace.getEventList()).thenReturn(events);
    when(trace.getEntityList()).thenReturn(entities);
  }

  private void addEdges(int sourceIdx, int targetIdx, Collection<Edge> collectionToAdd) {
    Edge edge = mock(Edge.class);
    when(edge.getSrcIndex()).thenReturn(sourceIdx);
    when(edge.getTgtIndex()).thenReturn(targetIdx);
    collectionToAdd.add(edge);
  }

  @Test
  void testOnMockedEvents() {
    ByteBuffer traceId = generateRandomId();

    String entityId1 = UUID.randomUUID().toString();
    Event e1 = getEvent(generateRandomId(), entityId1);
    Entity entity1 = getEntity(entityId1, "DOCKER_CONTAINER");
    RawSpan rawSpan1 = RawSpan.newBuilder().setCustomerId(CUSTOMER_ID).setTraceId(traceId)
        .setEvent(e1).setEntityList(List.of(entity1)).build();

    String entityId2 = UUID.randomUUID().toString();
    Event e2 = getEvent(generateRandomId(), entityId2);
    Entity entity2 = getEntity(entityId2, "K8S_POD");
    RawSpan rawSpan2 = RawSpan.newBuilder().setCustomerId(CUSTOMER_ID).setTraceId(traceId)
        .setEvent(e2).setEntityList(List.of(entity2)).build();

    // Make e2 as child of e1.
    ByteBuffer eventId1 = e1.getEventId();
    when(e2.getEventRefList()).thenReturn(Collections.singletonList(
        EventRef.newBuilder().setEventId(eventId1).setRefType(EventRefType.CHILD_OF)
            .setTraceId(traceId).build()));

    StructuredTrace trace = StructuredTraceBuilder
        .buildStructuredTraceFromRawSpans(List.of(rawSpan1, rawSpan2),
            traceId,
            CUSTOMER_ID);

    assertEquals(traceId, trace.getTraceId());
    assertEquals(CUSTOMER_ID, trace.getCustomerId());
    assertEquals(2, trace.getEventList().size());
    assertEquals(2, trace.getEntityList().size());

    StructuredTraceAssert.assertEntityEntityEdge(trace);
    StructuredTraceAssert.assertEventEventEdge(trace);
    StructuredTraceAssert.assertEntityEventEdges(trace);

    StructuredTraceGraph graph = StructuredTraceGraph.createGraph(trace);
    assertEquals(2, graph.getEventMap().size());
    assertEquals(1, graph.getRootEntities().size());
    assertEquals(1, graph.getRootEvents().size());
    assertTrue(graph.getChildIdsToParentIds().containsKey(e2.getEventId()));
    assertTrue(graph.getParentToChildEventIds().containsKey(e1.getEventId()));
    assertTrue(graph.getParentEntities(entity2).contains(entity1));
    assertEquals(e1, graph.getParentEvent(e2));
  }

  private ByteBuffer generateRandomId() {
    return ByteBuffer.wrap(UUID.randomUUID().toString().getBytes());
  }

  private Entity getEntity(String entityId, String entityType) {
    return Entity.newBuilder().setEntityId(entityId).setEntityType(entityType)
        .setEntityName(entityId).setCustomerId(CUSTOMER_ID)
        .setAttributes(Attributes.newBuilder().setAttributeMap(new HashMap<>()).build())
        .build();
  }

  private Event getEvent(ByteBuffer eventId, String entityId) {
    Event e = mock(Event.class);
    when(e.getEventId()).thenReturn(eventId);
    when(e.getEntityIdList()).thenReturn(Collections.singletonList(entityId));
    when(e.getAttributes())
        .thenReturn(Attributes.newBuilder().setAttributeMap(new HashMap<>()).build());
    when(e.getEnrichedAttributes())
        .thenReturn(Attributes.newBuilder().setAttributeMap(new HashMap<>()).build());
    when(e.getMetrics())
        .thenReturn(Metrics.newBuilder().setMetricMap(new HashMap<>()).build());
    return e;
  }

  @Test
  void test_createGraph_withDuplicateEntities() {
    // add duplicate entity
    entities.add(entities.get(0));

    createGraph_shouldCreateCorrectGraph();
  }

}
