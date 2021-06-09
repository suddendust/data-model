package org.hypertrace.core.datamodel.shared;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.StructuredTrace;

public class TraceEntitiesGraph {

  /* entity have many-to-many relationship */
  private final Map<String, List<Entity>> parentToChildrenEntities;
  private final Map<String, List<Entity>> childToParentEntities;

  /* these containers should be unmodifiable after initialization as we're exposing them via getters */
  private final Map<String, Entity> entityMap;
  private final Set<Entity> rootEntities;

  private TraceEntitiesGraph() {
    this.childToParentEntities = new HashMap<>();
    this.parentToChildrenEntities = new HashMap<>();
    this.entityMap = Maps.newHashMap();
    this.rootEntities = Sets.newHashSet();
  }

  /**
   * Create the instance with or without the full traversal graph includesFullGraph    Includes
   * computing the full traversal graph or not
   */
  public static TraceEntitiesGraph createGraph(StructuredTrace trace) {
    TraceEntitiesGraph graph = new TraceEntitiesGraph();
    graph.buildParentChildRelationship(trace);
    graph.processEntities(trace);
    return graph;
  }

  /**
   * @return an immutable set containing the root entities
   */
  public Set<Entity> getRootEntities() {
    return rootEntities;
  }


  public List<Entity> getParentEntities(Entity entity) {
    return childToParentEntities.get(entity.getEntityId());
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

  private void buildParentChildRelationship(StructuredTrace trace) {
    List<Entity> entities = trace.getEntityList();

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

  private void processEntities(StructuredTrace trace) {
    Set<String> childrenEntityIds = childToParentEntities.keySet();
    for (Entity entity : trace.getEntityList()) {
      entityMap.put(entity.getEntityId(), entity);
      if (!childrenEntityIds.contains(entity.getEntityId())) {
        // all non-child events are root events
        rootEntities.add(entity);
      }
    }
  }
}
