package me.doflamingo.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  EventRepository eventRepository;


  @Test
  public void createEvent() throws Exception {
    //given
    Event event = Event.builder()
                    .name("Spring")
                    .description("Spring Study")
                    .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
                    .endEventDateTime(LocalDateTime.of(2021,1,27,12,0))
                    .beginEnrollmentDateTime(LocalDateTime.of(2021,1,10,10,0))
                    .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,0))
                    .basePrice(100)
                    .maxPrice(300)
                    .limitOfEnrollment(100)
                    .eventStatus(EventStatus.DRAFT)
                    .location("강남역 D2 스타트업 팩토리")
                    .build();
    event.setId(100);
    when(eventRepository.save(event)).thenReturn(event);
    //when
    mockMvc.perform(post("/api/events")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaTypes.HAL_JSON+";charset=UTF-8")
      .content(objectMapper.writeValueAsString(event))
    )
    //then
    .andDo(print())
    .andExpect(status().isCreated())
    .andExpect(header().exists(HttpHeaders.LOCATION))
    .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE+";charset=UTF-8"))
    .andExpect(jsonPath("id").exists())
    ;

  }
}