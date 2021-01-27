package me.doflamingo.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.doflamingo.demorestapi.event.domain.Event;
import me.doflamingo.demorestapi.event.domain.EventStatus;
import me.doflamingo.demorestapi.event.dto.EventDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;
  

  @Test
  void createEvent() throws Exception {
    //given
    EventDto eventDto = EventDto.builder()
            .name("Spring")
            .description("Spring Study")
            .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
            .endEventDateTime(LocalDateTime.of(2021,1,27,12,0))
            .beginEnrollmentDateTime(LocalDateTime.of(2021,1,10,10,0))
            .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,0))
            .basePrice(100)
            .maxPrice(300)
            .limitOfEnrollment(100)
            .eventStatus(EventStatus.PUBLISHED)
            .location("강남역 D2 스타트업 팩토리")
            .build();
    //when
    mockMvc.perform(post("/api/events")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaTypes.HAL_JSON)
      .content(objectMapper.writeValueAsString(eventDto))
    )
    //then
    .andDo(print())
    .andExpect(status().isCreated())
    .andExpect(header().exists(HttpHeaders.LOCATION))
    .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE+ ";charset=UTF-8"))
    .andExpect(jsonPath("id").value(Matchers.not(100)))
    .andExpect(jsonPath("free").value(Matchers.not(true)))
    .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
    ;
  }
  @Test
  void createEvent_with_badRequest() throws Exception {
    //given
    Event event = Event.builder()
                    .id(100)
                    .name("Spring")
                    .description("Spring Study")
                    .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
                    .endEventDateTime(LocalDateTime.of(2021,1,27,12,0))
                    .beginEnrollmentDateTime(LocalDateTime.of(2021,1,10,10,0))
                    .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,0))
                    .basePrice(100)
                    .maxPrice(300)
                    .limitOfEnrollment(100)
                    .free(true)
                    .offline(false)
                    .eventStatus(EventStatus.PUBLISHED)
                    .location("강남역 D2 스타트업 팩토리")
                    .build();
    //when
    mockMvc.perform(post("/api/events")
                      .contentType(MediaType.APPLICATION_JSON)
                      .accept(MediaTypes.HAL_JSON)
                      .content(objectMapper.writeValueAsString(event))
    )
      //then
    .andDo(print())
    .andExpect(status().isBadRequest())
    ;
  }
}