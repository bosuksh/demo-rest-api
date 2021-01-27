package me.doflamingo.demorestapi.event;

import me.doflamingo.demorestapi.event.domain.Event;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

  @Test
  public void builder() {
    Event event = Event.builder().build();
    assertThat(event).isNotNull();
  }


  @Test
  public void javaBean() {
    //given
    Event event = new Event();
    String name = "Spring";
    String description = "SpringBoot Rest API";
    //when
    event.setName(name);
    event.setDescription(description);
    //then
    assertThat(event.getName()).isEqualTo(name);
    assertThat(event.getDescription()).isEqualTo(description);
  }

}