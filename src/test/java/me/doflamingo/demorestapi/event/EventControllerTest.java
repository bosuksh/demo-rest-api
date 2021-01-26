package me.doflamingo.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void createEvent() throws Exception {
    //given
    Event event = Event.builder()
                    .name("Spring")
                    .description("Spring Study")
                    .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
                    .endEventDateTime(LocalDateTime.of(2021,1,27,12,00))
                    .beginEnrollmentDateTime(LocalDateTime.of(2021,1,10,10,00))
                    .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,00))
                    .basePrice(100)
                    .maxPrice(300)
                    .limitOfEnrollment(100)
                    .eventStatus(EventStatus.DRAFT)
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
    .andExpect(status().isCreated())
    .andExpect(jsonPath("id").exists())
    ;

  }
}