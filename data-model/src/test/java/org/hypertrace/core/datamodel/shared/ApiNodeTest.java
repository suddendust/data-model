package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.hypertrace.core.datamodel.Event;
import org.junit.jupiter.api.Test;

public class ApiNodeTest {
  @Test
  public void testApiNodeMethods() {
    Event e1 = mock(Event.class);
    Event e2 = mock(Event.class);
    Event e3 = mock(Event.class);
    Event e4 = mock(Event.class);

    List<Event> apiNodeEvents = List.of(e1, e2, e3, e4);
    List<Event> exitEvents = List.of(e4);
    ApiNode<Event> apiNode = new ApiNode<>(e1, apiNodeEvents, e1, exitEvents);
    assertEquals(e1, apiNode.getHeadEvent());
    assertEquals(apiNodeEvents, apiNode.getEvents());
    assertEquals(Optional.of(e1), apiNode.getEntryApiBoundaryEvent());
    assertEquals(exitEvents, apiNode.getExitApiBoundaryEvents());
  }

  @Test
  public void testApiNodeEqualsAndHashcode() {
    Event e1 = mock(Event.class);
    Event e2 = mock(Event.class);
    Event e3 = mock(Event.class);
    Event e4 = mock(Event.class);

    List<Event> apiNodeEvents = List.of(e1, e2, e3, e4);
    List<Event> exitEvents = List.of(e4);
    ApiNode<Event> apiNode1 = new ApiNode<>(e1, apiNodeEvents, e1, exitEvents);
    ApiNode<Event> apiNode2 = new ApiNode<>(e2, apiNodeEvents, null, exitEvents);
    ApiNode<Event> apiNode3 = new ApiNode<>(e1, apiNodeEvents, e1, exitEvents);

    assertEquals(Optional.empty(), apiNode2.getEntryApiBoundaryEvent());

    assertEquals(apiNode1, apiNode1);
    assertNotEquals(apiNode1, apiNode2);
    assertEquals(apiNode1, apiNode3);

    assertEquals(apiNode1.hashCode(), apiNode1.hashCode());
    assertNotEquals(apiNode1.hashCode(), apiNode2.hashCode());
    assertEquals(apiNode1.hashCode(), apiNode3.hashCode());

    System.out.println(apiNode1);
    assertNotNull(apiNode1.toString());

    assertNotEquals(null, apiNode1);
    assertNotEquals(12, apiNode2);
    assertNotEquals(new ApiNode<>(e2, apiNodeEvents, e1, exitEvents), apiNode1);
    assertNotEquals(new ApiNode<>(e1, List.of(e1, e4), e1, exitEvents), apiNode1);
    assertNotEquals(new ApiNode<>(e1, apiNodeEvents, e1, List.of(e3, e4)), apiNode1);
  }
}
