package org.hypertrace.core.datamodel.shared;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

public class ApiNode {

  private final StructuredTrace trace;
  private final Event entryApiBoundaryEvent;
  private final List<Event> exitApiBoundaryEvents;

  public ApiNode(StructuredTrace trace, Event apiEntryEvent, List<Event> exitEvents) {
    this.trace = trace;
    this.entryApiBoundaryEvent = apiEntryEvent;
    this.exitApiBoundaryEvents = exitEvents;
  }

  /**
   * Returns the first span in the Api trace. This span could be an API entry or an intermediate
   * service or exit span.
   */
  public Event getHeadSpan() {
    Objects.checkIndex(0, trace.getEventList().size());
    return trace.getEventList().get(0);
  }

  public StructuredTrace getTrace() {
    return trace;
  }

  /**
   * Returns an optional API ENTRY span from this Api node. It's optional because, based on the
   * instrumentation, there could be only Exit call from a service and no Entry.
   */
  public Optional<Event> getEntryApiBoundaryEvent() {
    return Optional.ofNullable(entryApiBoundaryEvent);
  }

  public List<Event> getExitApiBoundaryEvents() {
    return exitApiBoundaryEvents;
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
    return Objects.equals(trace, apiNode.trace) &&
        Objects.equals(entryApiBoundaryEvent, apiNode.entryApiBoundaryEvent) &&
        Objects.equals(exitApiBoundaryEvents, apiNode.exitApiBoundaryEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trace, entryApiBoundaryEvent, exitApiBoundaryEvents);
  }

  @Override
  public String toString() {
    return "ApiNode{" +
        "trace=" + trace +
        ", entryApiBoundaryEvent=" + entryApiBoundaryEvent +
        ", exitApiBoundaryEvents=" + exitApiBoundaryEvents +
        '}';
  }
}
