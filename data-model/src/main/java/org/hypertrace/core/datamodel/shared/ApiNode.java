package org.hypertrace.core.datamodel.shared;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.avro.generic.GenericRecord;

public class ApiNode<T extends GenericRecord> {

  private final T entryApiBoundaryEvent;
  private final List<T> exitApiBoundaryEvents;
  private final T headEvent;
  private final List<T> events;

  /**
   * This creates an API Node(surprise) which contains all the events under an API call within a
   * single service. The call could be external or internal.
   *
   * @param headEvent: The event that defines the API call. The head of the API Trace.
   * @param events: All events under the API call including the head event.
   * @param apiEntryEvent: The Entry API event. Equals the head event if the head event is an Entry
   *     Event. Otherwise it's null.
   * @param exitEvents: The exit events into another service and API from this API call.
   */
  public ApiNode(T headEvent, List<T> events, T apiEntryEvent, List<T> exitEvents) {
    this.entryApiBoundaryEvent = apiEntryEvent;
    this.exitApiBoundaryEvents = exitEvents;
    this.headEvent = headEvent;
    this.events = events;
  }

  /**
   * Returns the first span in the Api trace. This span could be an API entry or an intermediate
   * service or exit span.
   */
  public T getHeadEvent() {
    return headEvent;
  }

  /**
   * Returns an optional API ENTRY span from this Api node. It's optional because, based on the
   * instrumentation, there could be only Exit call from a service and no Entry.
   */
  public Optional<T> getEntryApiBoundaryEvent() {
    return Optional.ofNullable(entryApiBoundaryEvent);
  }

  public List<T> getExitApiBoundaryEvents() {
    return exitApiBoundaryEvents;
  }

  public List<T> getEvents() {
    return events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiNode apiNode = (ApiNode) o;
    return Objects.equals(entryApiBoundaryEvent, apiNode.entryApiBoundaryEvent)
        && Objects.equals(exitApiBoundaryEvents, apiNode.exitApiBoundaryEvents)
        && Objects.equals(headEvent, apiNode.headEvent)
        && Objects.equals(events, apiNode.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryApiBoundaryEvent, exitApiBoundaryEvents, headEvent, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ApiNode{")
        .append("entryApiBoundaryEvent=")
        .append(entryApiBoundaryEvent)
        .append(", exitApiBoundaryEvents=")
        .append(exitApiBoundaryEvents)
        .append(", headEvent=")
        .append(headEvent)
        .append(", events")
        .append(events)
        .append('}');
    return sb.toString();
  }
}
