package org.hypertrace.core.datamodel.shared.trace;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.RawSpan;
import org.hypertrace.core.datamodel.Resource;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.junit.jupiter.api.Test;

class StructuredTraceBuilderTest {
  private static final String CUSTOMER_ID = "customer-id";
  private static final AtomicInteger CREATION_COUNTER = new AtomicInteger(0);
  private static final ByteBuffer TRACE_ID = ByteBuffer.wrap("trace-id".getBytes());

  @Test
  void correctlyDematerializesResourcesFromRawSpans() {
    Resource firstHostResource = buildResource(Map.of("host", "first-host"));
    Resource duplicateFirstHostResource = buildResource(Map.of("host", "first-host"));
    Resource secondHostResource = buildResource(Map.of("host", "second-host"));
    Resource firstHostExtendedResource =
        buildResource(Map.of("host", "first-host", "other-key", "other-value"));

    List<RawSpan> rawSpanList =
        List.of(
            this.buildRawSpanHoldingResource("first", firstHostResource),
            this.buildRawSpanHoldingResource("second", secondHostResource),
            this.buildRawSpanHoldingResource("third", duplicateFirstHostResource),
            this.buildRawSpanHoldingResource("fourth", null),
            this.buildRawSpanHoldingResource("fifth", firstHostExtendedResource));

    StructuredTrace trace =
        StructuredTraceBuilder.buildStructuredTraceFromRawSpans(rawSpanList, TRACE_ID, CUSTOMER_ID);

    assertEquals(
        List.of(firstHostResource, secondHostResource, firstHostExtendedResource),
        trace.getResourceList());

    assertEventMatches(trace.getEventList().get(0), "first", 0);
    assertEventMatches(trace.getEventList().get(1), "second", 1);
    assertEventMatches(trace.getEventList().get(2), "third", 0);
    assertEventMatches(trace.getEventList().get(3), "fourth", -1);
    assertEventMatches(trace.getEventList().get(4), "fifth", 2);
  }

  private RawSpan buildRawSpanHoldingResource(String id, @Nullable Resource resource) {
    RawSpan.Builder builder =
        RawSpan.newBuilder()
            .setCustomerId(CUSTOMER_ID)
            .setTraceId(TRACE_ID)
            .setEvent(
                Event.newBuilder()
                    .setEventId(ByteBuffer.wrap(id.getBytes()))
                    .setStartTimeMillis(
                        CREATION_COUNTER.getAndIncrement()) // Used for predictable ordering
                    .setCustomerId(CUSTOMER_ID)
                    .build());

    if (nonNull(resource)) {
      builder.setResource(resource);
    }
    return builder.build();
  }

  private void assertEventMatches(Event event, String id, int resourceIndex) {
    assertEquals(id, Charset.defaultCharset().decode(event.getEventId()).toString());
    assertEquals(resourceIndex, event.getResourceIndex());
  }

  private Resource buildResource(Map<String, String> resourceMap) {
    return resourceMap.entrySet().stream()
        .collect(
            Collectors.collectingAndThen(
                Collectors.toMap(
                    Entry::getKey, entry -> AttributeValueCreator.create(entry.getValue())),
                map ->
                    Resource.newBuilder()
                        .setAttributes(Attributes.newBuilder().setAttributeMap(map).build())
                        .build()));
  }
}
