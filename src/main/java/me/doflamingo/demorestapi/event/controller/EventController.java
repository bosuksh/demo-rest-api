package me.doflamingo.demorestapi.event.controller;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.error.ErrorResource;
import me.doflamingo.demorestapi.event.domain.Event;
import me.doflamingo.demorestapi.event.domain.EventResource;
import me.doflamingo.demorestapi.event.dto.EventDto;
import me.doflamingo.demorestapi.event.repository.EventRepository;
import me.doflamingo.demorestapi.event.validator.EventValidator;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
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
    if(errors.hasErrors()) {
      return badRequest(errors);
    }
    //@Valid에 해당하는 것 처리 이후 다른 validate도 수행해야한다.
    eventValidator.validate(eventDto,errors);

    if(errors.hasErrors()) {
      return badRequest(errors);
    }

    Event event = modelMapper.map(eventDto,Event.class);
    event.update();
    Event newEvent = eventRepository.save(event);

    WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    URI uri = selfLinkBuilder.toUri();
    EventResource eventResource = new EventResource(newEvent);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(Link.of("/docs/index.html#resources-create-event").withRel("profile"));

    return ResponseEntity
            .created(uri)
            .body(eventResource);
  }

  private ResponseEntity<ErrorResource> badRequest(Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorResource(errors));
  }

}
