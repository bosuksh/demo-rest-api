package me.doflamingo.demorestapi.event.controller;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.event.domain.Event;
import me.doflamingo.demorestapi.event.dto.EventDto;
import me.doflamingo.demorestapi.event.repository.EventRepository;
import me.doflamingo.demorestapi.event.validator.EventValidator;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

  private final EventRepository eventRepository;
  private final ModelMapper modelMapper;
  private final EventValidator eventValidator;

  @PostMapping
  public ResponseEntity<?> createEvents(@RequestBody @Valid EventDto eventDto, Errors errors) {

    eventValidator.validate(eventDto,errors);

    if(errors.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }

    Event event = modelMapper.map(eventDto,Event.class);
    Event newEvent = eventRepository.save(event);
    URI uri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity
            .created(uri)
            .body(newEvent);
  }

}
