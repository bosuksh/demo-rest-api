package me.doflamingo.demorestapi.event.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
public class Event {

  private Integer id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location; //(optional)
  private int basePrice;  //(optional)
  private int maxPrice;   //(optional)
  private int limitOfEnrollment;

  private boolean offline;
  private boolean free;
  private EventStatus eventStatus = EventStatus.DRAFT;
}