package org.hypertrace.core.datamodel.entity;

import java.util.List;

public class Graph {

  private List<Integer> root_nodes;
  private List<Integer> adjacency_list;

  public List<Integer> getRootNodes() {
    return root_nodes;
  }

  public List<Integer> getAdjacencyList() {
    return adjacency_list;
  }
}
