package me.doflamingo.demorestapi.event;

import junitparams.JUnitParamsRunner;
import me.doflamingo.demorestapi.events.domain.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

  @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
  @MethodSource("parametersForFreeTest")
  public void freeTest(int basePrice, int maxPrice, boolean isFree) throws Exception {
    //given
    Event event = Event.builder()
                    .basePrice(basePrice)
                    .maxPrice(maxPrice)
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isEqualTo(isFree);
  }

  private static Object[] parametersForFreeTest() {
    return new Object[] {
      new Object[] {0, 0, true},
      new Object[] {0, 100, false},
      new Object[] {100, 200, false},
      new Object[] {100, 0, false}
    };
  }

  @ParameterizedTest(name = "{index} => location={0}, isOffline={1}")
  @MethodSource("parameterForOfflineTest")
  public void offlineTest(String location, boolean isOffline) throws Exception {
    //given
    Event event = Event.builder()
                    .location(location)
                    .build();
    //when
    event.update();
    //then
    assertThat(event.isOffline()).isEqualTo(isOffline);
  }

  private static Object[] parameterForOfflineTest() {
    return new Object[] {
      new Object[] {"서울", true},
      new Object[] {null, false},
      new Object[] {" ", false},
      new Object[] {"", false},
    };
  }
}