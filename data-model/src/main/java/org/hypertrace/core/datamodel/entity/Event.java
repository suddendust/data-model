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
}
