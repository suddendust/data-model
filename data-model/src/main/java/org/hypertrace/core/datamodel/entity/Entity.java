package org.hypertrace.core.datamodel.entity;

public class Entity {
  private String customer_id;
  private String entity_id;
  private String entity_type;
  private String entity_name;
  private Attributes attributes;
  private EntityIds related_entity_ids;

  public String getCustomerId() {
    return customer_id;
  }

  public String getEntityId() {
    return entity_id;
  }

  public String getEntityType() {
    return entity_type;
  }

  public String getEntityName() {
    return entity_name;
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public EntityIds getRelatedEntityIds() {
    return related_entity_ids;
  }
}
