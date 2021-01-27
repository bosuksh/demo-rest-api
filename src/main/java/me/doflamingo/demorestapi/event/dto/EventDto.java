package me.doflamingo.demorestapi.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.doflamingo.demorestapi.event.domain.EventStatus;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter
public class EventDto {
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
    private EventStatus eventStatus = EventStatus.DRAFT;
}
