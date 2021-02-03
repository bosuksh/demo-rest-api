package me.doflamingo.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.doflamingo.demorestapi.common.RestDocsCustomizer;
import me.doflamingo.demorestapi.event.domain.Event;
import me.doflamingo.demorestapi.event.domain.EventStatus;
import me.doflamingo.demorestapi.event.dto.EventDto;
import me.doflamingo.demorestapi.event.repository.EventRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsCustomizer.class)
@ActiveProfiles("test")
class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  EventRepository eventRepository;

  @Test
  @DisplayName("정상 Request")
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
    .andExpect(jsonPath("free").value(false))
    .andExpect(jsonPath("offline").value(true))
    .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
    .andDo(document("create-event"
        ,links(linkWithRel("self").description("link to self")
        ,linkWithRel("query-events").description("link to query event list")
        ,linkWithRel("update-event").description("link to update an existing event")
        ,linkWithRel("profile").description("link to profile")
        ),
      requestHeaders(
        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type"),
        headerWithName(HttpHeaders.ACCEPT).description("accept")
      ),
      requestFields(
        fieldWithPath("name").description("행사명"),
        fieldWithPath("description").description("행사 설명"),
        fieldWithPath("beginEnrollmentDateTime").description("행사 등록 시작 시간"),
        fieldWithPath("closeEnrollmentDateTime").description("행사 등록 마감 시간"),
        fieldWithPath("beginEventDateTime").description("행사 시작 시간"),
        fieldWithPath("endEventDateTime").description("행사 종료 시간"),
        fieldWithPath("location").description("장소"),
        fieldWithPath("basePrice").description("기본 가격"),
        fieldWithPath("maxPrice").description("최대 가격"),
        fieldWithPath("limitOfEnrollment").description("최대 참가 인원")
      ),
      responseHeaders(
        headerWithName(HttpHeaders.LOCATION).description("생성된 uri 주소"),
        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
      ),
      responseFields(
        fieldWithPath("id").description("ID"),
        fieldWithPath("name").description("행사명"),
        fieldWithPath("description").description("행사 설명"),
        fieldWithPath("beginEnrollmentDateTime").description("행사 등록 시작 시간"),
        fieldWithPath("closeEnrollmentDateTime").description("행사 등록 마감 시간"),
        fieldWithPath("beginEventDateTime").description("행사 시작 시간"),
        fieldWithPath("endEventDateTime").description("행사 종료 시간"),
        fieldWithPath("location").description("장소"),
        fieldWithPath("basePrice").description("기본 가격"),
        fieldWithPath("maxPrice").description("최대 가격"),
        fieldWithPath("limitOfEnrollment").description("최대 참가 인원"),
        fieldWithPath("offline").description("오프라인 여부"),
        fieldWithPath("free").description("무료 여부"),
        fieldWithPath("eventStatus").description("이벤트 상태"),
        fieldWithPath("_links.self.href").description("link to self"),
        fieldWithPath("_links.query-events.href").description("link to query event list"),
        fieldWithPath("_links.update-event.href").description("link to update an existing event"),
        fieldWithPath("_links.profile.href").description("link to profile")
      )
    ))
    ;

    //given
    eventDto.setLocation(null);
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
      .andExpect(jsonPath("free").value(false))
      .andExpect(jsonPath("offline").value(false))
      .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
    ;
  }

  @Test
  @DisplayName("DTO에 존재하지 않는 필드를 추가해서 Request")
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

  @Test
  @DisplayName("빈 입력값으로 요청")
  public void createEvent_with_empty_input() throws Exception{
    EventDto eventDto = EventDto.builder().build();
    mockMvc.perform(post("/api/events")
      .content(objectMapper.writeValueAsString(eventDto))
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaTypes.HAL_JSON)
    )
    .andDo(print())
    .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("DTO에 가격 필드를 잘못 설정해서 Request")
  void createEvent_with_wrongPrice() throws Exception {
    //given
    EventDto eventDto = EventDto.builder()
                          .name("Spring")
                          .description("Spring Study")
                          .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
                          .endEventDateTime(LocalDateTime.of(2021,1,27,12,0))
                          .beginEnrollmentDateTime(LocalDateTime.of(2021,1,10,10,0))
                          .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,0))
                          .basePrice(400)
                          .maxPrice(300)
                          .limitOfEnrollment(100)
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
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("errors[0].code").exists())
      .andExpect(jsonPath("errors[0].objectName").exists())
      .andExpect(jsonPath("errors[0].defaultMessage").exists())
      .andExpect(jsonPath("_links.index").exists())
    ;
  }

  @Test
  @DisplayName("DTO에 등록 시작 시간 필드를 잘못 설정해서 Request")
  void createEvent_with_wrongBeginEnrollmentTime() throws Exception {
    //given
    EventDto eventDto = EventDto.builder()
                          .name("Spring")
                          .description("Spring Study")
                          .beginEnrollmentDateTime(LocalDateTime.of(2021,1,21,10,0))
                          .closeEnrollmentDateTime(LocalDateTime.of(2021,1,20,10,0))
                          .beginEventDateTime(LocalDateTime.of(2021,1,27,10,0))
                          .endEventDateTime(LocalDateTime.of(2021,1,27,12,0))
                          .basePrice(300)
                          .maxPrice(400)
                          .limitOfEnrollment(100)
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
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("errors[0].code").exists())
      .andExpect(jsonPath("errors[0].objectName").exists())
      .andExpect(jsonPath("errors[0].defaultMessage").exists())
    ;
  }
  @Test
  @DisplayName("30개 리스트를 보여주고 PageSize 10개 2번째 페이지")
  public void queryEvents() throws Exception {
    //given
    IntStream.range(0,30).forEach(this::generateEvent);
    //when
    mockMvc.perform(get("/api/events")
        .param("page","1")
        .param("size","10")
        .param("sort","name,DESC"))
      //then
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_links").exists())
      .andExpect(jsonPath("page").exists())
      .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
      .andExpect(jsonPath("_links.self").exists())
      .andExpect(jsonPath("_links.profile").exists())
      .andDo(document("query-events",
        links(linkWithRel("self").description("link to self")
        ,linkWithRel("first").description("link to first page")
        ,linkWithRel("prev").description("link to prev page")
        ,linkWithRel("next").description("link to next page")
        ,linkWithRel("last").description("link to last page")
        ,linkWithRel("profile").description("link to profile")
        )
      ))
    ;
  }

  @Test
  @DisplayName("리스트 조회 성공")
  public void getEvent() throws Exception {
    //given
    generateEvent(1);
    //when
    mockMvc.perform(get("/api/events/1"))
    //then
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("id").exists())
    .andExpect(jsonPath("name").exists())
    .andExpect(jsonPath("_links.profile").exists())
    .andExpect(jsonPath("_links.self").exists())
    ;
  }

  @Test
  @DisplayName("리스트 조회 실패")
  public void getEvent404() throws Exception {
    //given
    generateEvent(1);
    //when
    mockMvc.perform(get("/api/events/400"))
    //then
    .andDo(print())
    .andExpect(status().isNotFound())
    ;
  }



  private Event generateEvent(int index) {
    Event event = Event.builder()
                    .name("Event "+index)
                    .description("Event Description"+ index)
                    .build();
    return eventRepository.save(event);
  }
}