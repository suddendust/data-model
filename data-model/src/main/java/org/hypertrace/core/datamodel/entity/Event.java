package org.hypertrace.core.datamodel.entity;

import java.nio.ByteBuffer;
import java.util.List;
import org.hypertrace.core.datamodel.entity.eventfields.grpc.Grpc;
import org.hypertrace.core.datamodel.entity.eventfields.http.Http;
import org.hypertrace.core.datamodel.entity.eventfields.jaeger.JaegerFields;
import org.hypertrace.core.datamodel.entity.eventfields.rpc.Rpc;
import org.hypertrace.core.datamodel.entity.eventfields.sql.Sql;

public class Event {

  private String customer_id;
  private ByteBuffer event_id;
  private String event_name;
  private List<String> entity_id_list;
  private Integer resource_index;
  private Attributes attributes;
  private Long start_time_millis;
  private Long end_time_millis;
  private Metrics metrics;
  private List<EventRef> event_ref_list;
  private Attributes enriched_attributes;
  private JaegerFields jaegerFields;
  private Http http;
  private Grpc grpc;
  private Sql sql;
  private String service_name;
  private Rpc rpc;

  public String getCustomerId() {
    return customer_id;
  }

  public ByteBuffer getEventId() {
    return event_id;
  }

  public String getEventName() {
    return event_name;
  }

  public List<String> getEntityIdList() {
    return entity_id_list;
  }

  public Integer getResourceIndex() {
    return resource_index;
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public Long getStartTimeMillis() {
    return start_time_millis;
  }

  public Long getEndTimeMillis() {
    return end_time_millis;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  public List<EventRef> getEventRefList() {
    return event_ref_list;
  }

  public Attributes getEnrichedAttributes() {
    return enriched_attributes;
  }

  public JaegerFields getJaegerFields() {
    return jaegerFields;
  }

  public Http getHttp() {
    return http;
  }

  public Grpc getGrpc() {
    return grpc;
  }

  public Sql getSql() {
    return sql;
  }

  public String getServiceName() {
    return service_name;
  }

  public Rpc getRpc() {
    return rpc;
  }

  public void setCustomerId(String customerId) {
    this.customer_id = customerId;
  }

  public void setEventId(ByteBuffer eventId) {
    this.event_id = eventId;
  }

  public void setEventName(String eventName) {
    this.event_name = eventName;
  }

  public void setEntityIdList(List<String> entityIdList) {
    this.entity_id_list = entityIdList;
  }

  public void setResourceIndex(Integer resourceIndex) {
    this.resource_index = resourceIndex;
  }

  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
  }

  public void setStartTimeMillis(Long startTimeMillis) {
    this.start_time_millis = startTimeMillis;
  }

  public void setEndTimeMillis(Long endTimeMillis) {
    this.end_time_millis = endTimeMillis;
  }

  public void setMetrics(Metrics metrics) {
    this.metrics = metrics;
  }

  public void setEventRefList(List<EventRef> eventRefList) {
    this.event_ref_list = eventRefList;
  }

  public void setEnrichedAttributes(Attributes enrichedAttributes) {
    this.enriched_attributes = enrichedAttributes;
  }

  public void setJaegerFields(
      JaegerFields jaegerFields) {
    this.jaegerFields = jaegerFields;
  }

  public void setHttp(Http http) {
    this.http = http;
  }

  public void setGrpc(Grpc grpc) {
    this.grpc = grpc;
  }

  public void setSql(Sql sql) {
    this.sql = sql;
  }

  public void setServiceName(String serviceName) {
    this.service_name = serviceName;
  }

  public void setRpc(Rpc rpc) {
    this.rpc = rpc;
  }
}
