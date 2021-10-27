package org.hypertrace.core.datamodel.shared.trace;

import static java.util.Objects.nonNull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.EdgeType;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.EventRef;
import org.hypertrace.core.datamodel.MetricValue;
import org.hypertrace.core.datamodel.Metrics;
import org.hypertrace.core.datamodel.RawSpan;
import org.hypertrace.core.datamodel.Resource;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.StructuredTrace.Builder;
import org.hypertrace.core.datamodel.Timestamps;
import org.hypertrace.core.datamodel.shared.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructuredTraceBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(StructuredTraceBuilder.class);
  private long traceStartTime;
  private long traceEndTime;
  private final String customerId;
  private final ByteBuffer traceId;
  private final List<Event> eventList;

  private final Map<String, Entity> entityMap;
  private final Map<ByteBuffer, Event> eventMap;
  private final List<Resource> resourceList;
  private ArrayList<ByteBuffer> orderedEventNodes;
  private ArrayList<String> orderedEntityNodes;

  private final Map<ByteBuffer, List<ByteBuffer>> eventEventConnectionMap;
  private final Map<String, List<String>> entityEntityConnectionMap;
  private final Map<String, List<ByteBuffer>> entityEventConnectionMap;

  private boolean isPartialTrace;
  private final BiMap<String, Integer> entityIdMapping;
  private final BiMap<ByteBuffer, Integer> eventIdMapping;
  private final Set<ByteBuffer> missingEventIdSet;
  private final Timestamps timestamps;

  public StructuredTraceBuilder(
      List<Event> eventList,
      Map<String, Entity> entityMap,
      String customerId,
      ByteBuffer traceId,
      Timestamps timestamps,
      List<Resource> resourceList) {
    this.eventList = eventList;
    this.resourceList = resourceList;
    this.customerId = customerId;
    this.traceId = traceId;
    this.entityMap = Map.copyOf(entityMap);
    eventMap = new HashMap<>();
    eventEventConnectionMap = new HashMap<>();
    entityEntityConnectionMap = new HashMap<>();
    entityEventConnectionMap = new HashMap<>();
    entityIdMapping = HashBiMap.create();
    eventIdMapping = HashBiMap.create();
    missingEventIdSet = new HashSet<>();
    isPartialTrace = false;
    traceStartTime = Long.MAX_VALUE;
    traceEndTime = Long.MIN_VALUE;
    this.timestamps = timestamps;
  }

  public StructuredTrace buildStructuredTrace() {

    long execStartTime = System.currentTimeMillis();

    for (Event event : eventList) {
      // Create the event Entity Map
      eventMap.put(event.getEventId(), event);
      if (event.getStartTimeMillis() < traceStartTime) {
        traceStartTime = event.getStartTimeMillis();
      }
      if (event.getEndTimeMillis() > traceEndTime) {
        traceEndTime = event.getEndTimeMillis();
      }
    }

    // process the event ref's and build edges
    for (Event event : eventMap.values()) {
      for (String entityId : event.getEntityIdList()) {
        entityEventConnectionMap.putIfAbsent(entityId, new ArrayList<>());
        entityEventConnectionMap.get(entityId).add(event.getEventId());
      }
      // create entity->entity Edge and event->event edge
      processEventRefList(traceId, event);
    }

    // Find root nodes for event and entity
    // First add all nodes as root nodes
    // then iterate through all edges and remove the child nodes
    // at the end only the nodes without parents will be left.
    // NOTE: the reason we can't just pick nodes with node spanRef as there can be missing spans
    // So its possible to have multiple root nodes when isPartialTrace = true
    Set<String> rootEntityNodes = new HashSet<>(entityMap.keySet());
    for (Entry<String, List<String>> entry : entityEntityConnectionMap.entrySet()) {
      for (String entityId : entry.getValue()) {
        rootEntityNodes.remove(entityId);
      }
    }

    Set<ByteBuffer> rootEventNodes = new HashSet<>(eventMap.keySet());
    for (Entry<ByteBuffer, List<ByteBuffer>> entry : eventEventConnectionMap.entrySet()) {
      for (ByteBuffer eventId : entry.getValue()) {
        rootEventNodes.remove(eventId);
      }
    }

    ArrayList<ByteBuffer> rootEventNodesOrdered = new ArrayList<>(rootEventNodes);
    // sort the rootEventNodes by time. Ideally, there should be only one root Event Node
    // We can see multiple root nodes if some span events are missing from the trace
    if (rootEventNodes.size() > 0) {
      rootEventNodesOrdered.sort(
          Comparator.comparingLong(e -> eventMap.get(e).getStartTimeMillis()));
    }

    orderedEventNodes = new ArrayList<>();
    orderedEntityNodes = new ArrayList<>();

    int eventIndex = 0;
    int entityIndex = 0;

    // BFS order traversal
    for (ByteBuffer rootEventId : rootEventNodesOrdered) {
      // Build Graph for each root node
      // queue for BFS traversal
      LinkedList<ByteBuffer> eventIdQueue = new LinkedList<>();
      eventIdQueue.add(rootEventId);
      while (!eventIdQueue.isEmpty()) {
        ByteBuffer eventId = eventIdQueue.pop();
        orderedEventNodes.add(eventId);
        eventIdMapping.put(eventId, eventIndex++);

        // assign entity id's as well if they are not assigned
        for (String entityId : eventMap.get(eventId).getEntityIdList()) {
          if (!entityIdMapping.containsKey(entityId)) {
            entityIdMapping.put(entityId, entityIndex++);
            orderedEntityNodes.add(entityId);
          }
        }
        List<ByteBuffer> childEventIdList = eventEventConnectionMap.get(eventId);
        if (childEventIdList != null && childEventIdList.size() > 0) {
          // sort the childEventId by their start time
          childEventIdList.sort(
              Comparator.comparingLong(e -> eventMap.get(e).getStartTimeMillis()));
          eventIdQueue.addAll(childEventIdList);
        }
      }
    }
    StructuredTrace structuredTrace = build();
    long execEndTime = System.currentTimeMillis();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "Generated structuredTrace from events list in {} ms. Output = {}",
          (execEndTime - execStartTime),
          structuredTrace);
    }
    return structuredTrace;
  }

  private StructuredTrace build() {
    // start building the Structured Trace Proto object
    Builder builder = StructuredTrace.newBuilder();
    builder.setCustomerId(customerId);
    builder.setTraceId(traceId);
    builder.setEventList(new ArrayList<>());
    builder.setEntityList(new ArrayList<>());
    builder.setEventEdgeList(new ArrayList<>());
    builder.setEntityEdgeList(new ArrayList<>());
    builder.setEntityEventEdgeList(new ArrayList<>());
    builder.setResourceList(this.resourceList);

    // Node Builders
    // Initialize EVENT NODE
    for (ByteBuffer eventId : orderedEventNodes) {
      builder.getEventList().add(eventMap.get(eventId));
    }

    // Initialize ENTITY NODE builders
    for (String entityId : orderedEntityNodes) {
      builder.getEntityList().add(entityMap.get(entityId));
    }

    // Build Event Edge
    for (Entry<ByteBuffer, List<ByteBuffer>> entry : eventEventConnectionMap.entrySet()) {
      ByteBuffer parentEventId = entry.getKey();
      for (ByteBuffer childEventId : entry.getValue()) {
        Edge edge = buildEventEdge(parentEventId, childEventId);
        builder.getEventEdgeList().add(edge);
      }
    }

    // Build Entity-Entity Edge
    for (Entry<String, List<String>> entry : entityEntityConnectionMap.entrySet()) {
      String parentEntityId = entry.getKey();
      for (String childEntityId : entry.getValue()) {
        Edge edge = buildEntityEdge(parentEntityId, childEntityId);
        builder.getEntityEdgeList().add(edge);
      }
    }
    // Entity Event Edge
    for (Entry<String, List<ByteBuffer>> entry : entityEventConnectionMap.entrySet()) {
      String entityId = entry.getKey();
      for (ByteBuffer eventId : entry.getValue()) {
        Edge edge = buildEntityEventEdge(entityId, eventId);
        builder.getEntityEventEdgeList().add(edge);
      }
    }

    // Trace attributes
    HashMap<String, AttributeValue> attributeMap = new HashMap<>();
    if (isPartialTrace) {
      attributeMap.put("isPartial", AttributeValueCreator.create(isPartialTrace));
      attributeMap.put(
          "missingEventIds", AttributeValueCreator.createFromByteBuffers(missingEventIdSet));
    }

    // Trace Metrics
    HashMap<String, MetricValue> metricMap = new HashMap<>();
    long durationInMillis = traceEndTime - traceStartTime;
    // todo: define constants for these things.
    // todo: move these things to Annotation Framework
    metricMap.put("Duration", MetricValueCreator.create(durationInMillis));
    metricMap.put("CallCount", MetricValueCreator.create(1));

    builder.setTraceId(traceId);
    builder.setStartTimeMillis(traceStartTime);
    builder.setEndTimeMillis(traceEndTime);
    builder.setAttributes(new Attributes(attributeMap));
    builder.setMetrics(new Metrics(metricMap));
    builder.setTimestamps(timestamps);
    return builder.build();
  }

  private Edge buildEntityEdge(String parentEntityId, String childEntityId) {
    Edge.Builder edgeBuilder = Edge.newBuilder();
    edgeBuilder.setEdgeType(EdgeType.ENTITY_ENTITY);
    edgeBuilder.setSrcIndex(entityIdMapping.get(parentEntityId));
    edgeBuilder.setTgtIndex(entityIdMapping.get(childEntityId));
    Entity parentEntity = entityMap.get(parentEntityId);
    Entity childEntity = entityMap.get(childEntityId);
    // todo: define constants for these things.
    // todo: move these things to Annotation Framework
    HashMap<String, AttributeValue> Attributes = new HashMap<>();
    Attributes.put("CallerEntityType", AttributeValueCreator.create(parentEntity.getEntityType()));
    Attributes.put("CallerEntityName", AttributeValueCreator.create(parentEntity.getEntityName()));
    Attributes.put("CalleeEntityType", AttributeValueCreator.create(childEntity.getEntityType()));
    Attributes.put("CalleeEntityName", AttributeValueCreator.create(childEntity.getEntityName()));
    edgeBuilder.setAttributes(new Attributes(Attributes));

    // metrics
    edgeBuilder.setMetrics(new Metrics(new HashMap<>()));
    return edgeBuilder.build();
  }

  private Edge buildEntityEventEdge(String entityId, ByteBuffer eventId) {
    Edge.Builder edgeBuilder = Edge.newBuilder();
    edgeBuilder.setEdgeType(EdgeType.ENTITY_EVENT);
    edgeBuilder.setSrcIndex(entityIdMapping.get(entityId));
    edgeBuilder.setTgtIndex(eventIdMapping.get(eventId));
    edgeBuilder.setAttributes(Attributes.newBuilder().setAttributeMap(new HashMap<>()).build());
    edgeBuilder.setMetrics(Metrics.newBuilder().setMetricMap(new HashMap<>()).build());
    return edgeBuilder.build();
  }

  private Edge buildEventEdge(ByteBuffer parentEventId, ByteBuffer childEventId) {
    Edge.Builder edgeBuilder = Edge.newBuilder();
    edgeBuilder.setSrcIndex(eventIdMapping.get(parentEventId));
    edgeBuilder.setTgtIndex(eventIdMapping.get(childEventId));
    edgeBuilder.setEdgeType(EdgeType.EVENT_EVENT);
    Event parentEvent = eventMap.get(parentEventId);
    Event childEvent = eventMap.get(childEventId);

    edgeBuilder.setStartTimeMillis(parentEvent.getStartTimeMillis());
    edgeBuilder.setEndTimeMillis(parentEvent.getEndTimeMillis());

    // edge attributes
    HashMap<String, AttributeValue> attributeMap = new HashMap<>();

    List<String> callerEntityNames = new ArrayList<>();
    for (String entityId : parentEvent.getEntityIdList()) {
      callerEntityNames.add(entityMap.get(entityId).getEntityName());
    }

    List<String> calleeEntityNames = new ArrayList<>();
    for (String entityId : childEvent.getEntityIdList()) {
      calleeEntityNames.add(entityMap.get(entityId).getEntityName());
    }

    // todo: define constants for these things.
    // todo: move these things to Annotation Framework
    attributeMap.put("CallerEntityNames", AttributeValueCreator.create(callerEntityNames));
    attributeMap.put("CalleeEntityNames", AttributeValueCreator.create(calleeEntityNames));

    edgeBuilder.setAttributes(new Attributes(attributeMap));

    // edge metrics, only duration and call count for now
    HashMap<String, MetricValue> metricMap = new HashMap<>();
    long durationInMillis = parentEvent.getEndTimeMillis() - parentEvent.getStartTimeMillis();
    // todo: define constants for these things.
    // todo: move these things to Annotation Framework
    metricMap.put("Duration", MetricValueCreator.create(durationInMillis));
    metricMap.put("CallCount", MetricValueCreator.create(1));
    edgeBuilder.setMetrics(new Metrics(metricMap));

    return edgeBuilder.build();
  }

  private void processEventRefList(ByteBuffer traceId, Event event) {
    List<EventRef> eventRefList = event.getEventRefList();
    if (eventRefList == null) {
      return;
    }
    for (EventRef eventRef : eventRefList) {
      if (!eventRef.getTraceId().equals(traceId)) {
        // either this is an incomplete trace
        // or this referenced event belongs to another trace as of now
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(
              "Skipping referenced event since it belongs to another trace. EventRef.TraceId = {}  event.TraceId={}",
              HexUtils.getHex(eventRef.getTraceId()),
              HexUtils.getHex(traceId));
        }

        continue;
      }
      if (!eventMap.containsKey(eventRef.getEventId())) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(
              "Referenced eventId:{} is not part of the Trace {}. May be partial trace.",
              HexUtils.getHex(eventRef.getEventId()),
              HexUtils.getHex(eventRef.getTraceId()));
        }
        isPartialTrace = true;
        missingEventIdSet.add(eventRef.getEventId());
        // TODO: consider creating an empty event node?
        continue;
      }

      /*
      * For Follow from relationship:
      * As, both the construct `child_of` and `follow_from` represent parent-child relation in common
      * where in one case parent is interested in child span's result while in other case not.
      *
      * So, to support common behaviour, we will be establish link for `follow_from` as well.
      * commenting out this part.
      *
      *      if (!eventRef.getRefType().equals(EventRefType.CHILD_OF)) {
      *
      *   continue;
      * }
      * Ref: https://github.com/hypertrace/hypertrace/issues/234.
      Note: Ideally, an event should have a single parent. It would be either via child_of or using follow_from.
      */

      Event parentEvent = eventMap.get(eventRef.getEventId());

      // Add Event to Event Edge.
      eventEventConnectionMap
          .computeIfAbsent(parentEvent.getEventId(), e -> new ArrayList<>())
          .add(event.getEventId());

      // Create edge between all entity types
      for (String parentEventEntityId : parentEvent.getEntityIdList()) {
        Entity parentEventEntity = entityMap.get(parentEventEntityId);
        // unlikely but just in case, it should handle this gracefully
        if (parentEventEntity != null) {
          String parentEventEntityType = parentEventEntity.getEntityType();
          for (String childEventEntityId : event.getEntityIdList()) {
            // Add the edge only if the parent entity is not the same as the child entity
            // TODO: Self links are valid cases since we could get internal spans where both
            //  parent and child span belong to same entity. This should be solved later.
            if (parentEventEntityType != null && !parentEventEntityId.equals(childEventEntityId)) {
              // ADD ENTITY TO ENTITY EDGE
              entityEntityConnectionMap
                  .computeIfAbsent(parentEventEntityId, e -> new ArrayList<>())
                  .add(childEventEntityId);
            }
          }
        } else {
          LOGGER.warn(
              "Unable to find the parent entity from ids in the event. "
                  + "EventId: {}, EntityId: {}",
              HexUtils.getHex(parentEvent.getEventId()),
              parentEventEntityId);
        }
      }
    }
  }

  /**
   * Builds a StructuredTrace from the events in the rawSpanList. All the events are assumed to have
   * a traceId and customerId equal to the traceId and customerId parameters given to this method.
   * This method is used in raw spans grouper to group raw spans into a StructuredTrace.
   *
   * @return StructuredTrace
   */
  public static StructuredTrace buildStructuredTraceFromRawSpans(
      List<RawSpan> rawSpanList, ByteBuffer traceId, String customerId) {
    return buildStructuredTraceFromRawSpans(rawSpanList, traceId, customerId, null);
  }

  public static StructuredTrace buildStructuredTraceFromRawSpans(
      List<RawSpan> rawSpanList, ByteBuffer traceId, String customerId, Timestamps timestamps) {
    Map<String, Entity> entityMap = new HashMap<>();
    // Relying on insertion ordered keyset
    LinkedHashMap<Resource, Integer> resourceIndexMap = new LinkedHashMap<>();
    List<Event> eventList = new ArrayList<>();
    for (RawSpan rawSpan : rawSpanList) {
      eventList.add(rawSpan.getEvent());
      List<Entity> entitiesList = rawSpan.getEntityList();

      if (nonNull(rawSpan.getResource())) {
        // If a resource exists on span, record the resource and its index
        rawSpan
            .getEvent()
            .setResourceIndex(
                resourceIndexMap.computeIfAbsent(
                    rawSpan.getResource(), unused -> resourceIndexMap.size()));
      }
      for (Entity entity : entitiesList) {
        if (!entityMap.containsKey(entity.getEntityId())) {
          entityMap.put(entity.getEntityId(), entity);
        } else {
          // If the same entity is found across multiple Spans, merge the entity attributes
          Entity existingEntity = entityMap.get(entity.getEntityId());
          mergeAttributes(existingEntity, entity);
        }
      }
    }

    StructuredTraceBuilder structuredTraceBuilder =
        new StructuredTraceBuilder(
            eventList,
            entityMap,
            customerId,
            traceId,
            timestamps,
            List.copyOf(resourceIndexMap.keySet()));

    return structuredTraceBuilder.buildStructuredTrace();
  }

  /**
   * Merges attributes of same entity that comes at different spans If there's a conflict, the
   * attributes that comes in the later spans takes precedence
   */
  private static void mergeAttributes(Entity existingEntity, Entity newEntity) {
    Attributes mergedAttributes = new Attributes(new HashMap<>());
    if (existingEntity.getAttributes() != null) {
      mergedAttributes.getAttributeMap().putAll(existingEntity.getAttributes().getAttributeMap());
    }
    if (newEntity.getAttributes() != null) {
      mergedAttributes.getAttributeMap().putAll(newEntity.getAttributes().getAttributeMap());
    }
    existingEntity.setAttributes(mergedAttributes);
  }
}
