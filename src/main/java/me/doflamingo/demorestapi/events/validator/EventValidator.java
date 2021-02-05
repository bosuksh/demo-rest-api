package me.doflamingo.demorestapi.events.validator;

import me.doflamingo.demorestapi.events.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

  public void validate(EventDto eventDto, Errors errors) {
    priceValidate(eventDto, errors);
    timeValidate(eventDto, errors);
  }

  private void priceValidate(EventDto eventDto, Errors errors) {
    if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
      errors.rejectValue("basePrice","wrongValue","Base price is Wrong");
      errors.rejectValue("maxPrice","wrongValue","Max price is Wrong");
    }
  }

  private void timeValidate(EventDto eventDto, Errors errors) {
    LocalDateTime beginEnrollmentDateTime = eventDto.getBeginEnrollmentDateTime();
    LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
    LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
    LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();

    //등록 시작 시간은 등록 종료 시간, 행사 시작 시간, 행사 종료 시간 보다 빨라야 한다.
    if(beginEnrollmentDateTime.isAfter(closeEnrollmentDateTime) ||
         beginEnrollmentDateTime.isAfter(beginEventDateTime) ||
         beginEnrollmentDateTime.isAfter(endEventDateTime)) {
      errors.reject("beginEnrollmentTime", "BeginEnrollmentTime is wrong");
    }

    //등록 종료 시간은 행사 시작 시간과 행사 종료 시간보다 빨라야 한다.
    if(closeEnrollmentDateTime.isAfter(beginEventDateTime) ||
         closeEnrollmentDateTime.isAfter(endEventDateTime))
    {
      errors.reject("closeEnrollmentTime",  "CloseEnrollmentTime is wrong");
    }

    //행사 시작 시간은 행사 종료 시간보다 빨라야 한다.
    if(beginEventDateTime.isAfter(endEventDateTime)) {
      errors.reject("beginEventTime", "BeginEventTime is wrong");
    }
  }


}
