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
  @Test
  public void freeTest() throws Exception {
    /*
    * Free일 때
    * */
    //given
    Event event = Event.builder()
                    .basePrice(0)
                    .maxPrice(0)
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isTrue();

    /*
     * MaxPrice 존재할 때
     * */
    //given
    event = Event.builder()
              .basePrice(0)
              .maxPrice(100)
              .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isFalse();

    /*
     * basePrice 존재할 때
     * */
    //given
    event = Event.builder()
                    .basePrice(100)
                    .maxPrice(0)
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isFalse();
  }

  @Test
  public void offlineTest() throws Exception {
    //given
    Event event = Event.builder()
                    .location("서울")
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isOffline()).isTrue();

    //given
    event = Event.builder()
                    .location("")
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isOffline()).isFalse();

    //given
    event = Event.builder()
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isOffline()).isFalse();
  }

}