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
  private String entity_id_list;
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

  public String getEntityIdList() {
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
}
