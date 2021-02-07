package me.doflamingo.demorestapi.events.controller;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.error.ErrorResource;
import me.doflamingo.demorestapi.events.domain.Event;
import me.doflamingo.demorestapi.events.domain.EventResource;
import me.doflamingo.demorestapi.events.dto.EventDto;
import me.doflamingo.demorestapi.events.repository.EventRepository;
import me.doflamingo.demorestapi.events.validator.EventValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

  private final EventRepository eventRepository;
  private final EventValidator eventValidator;

  @PostMapping
  public ResponseEntity<?> createEvents(@RequestBody @Valid EventDto eventDto,
                                        BindingResult errors,
                                        @AuthenticationPrincipal(expression = "#this == 'anonymousUser'? null:account") Account currentUser) {
    if(errors.hasErrors()) {
      return badRequest(errors);
    }
    //@Valid에 해당하는 것 처리 이후 다른 validate도 수행해야한다.
    eventValidator.validate(eventDto,errors);

    if(errors.hasErrors()) {
      return badRequest(errors);
    }

    Event event = eventDtoToEntity(eventDto, new Event(), currentUser);
    Event newEvent = eventRepository.save(event);

    WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    URI uri = selfLinkBuilder.toUri();
    var eventResource = EventResource.of(newEvent);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(getProfileLink("resources-create-event"));

    return ResponseEntity
            .created(uri)
            .body(eventResource);
  }

  @GetMapping
  public ResponseEntity<?> queryEvents(Pageable pageable,
                                       PagedResourcesAssembler<Event> assembler,
                                       @AuthenticationPrincipal(expression = "#this == 'anonymousUser'? null:account") Account currentUser) {
    Page<Event> page = eventRepository.findAll(pageable);
    var entityModels = assembler.toModel(page, EventResource::of);
    entityModels.add(getProfileLink("resources-events-list"));
    if(currentUser != null) {
      entityModels.add(linkTo(EventController.class).withRel("create-event"));
    }
    return ResponseEntity.ok(entityModels);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getEvent(@PathVariable Integer id,
                                    @AuthenticationPrincipal(expression = "#this=='anonymousUser' ? null : account") Account currentUser) {
    Optional<Event> optionalEvent = eventRepository.findById(id);
    if(optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Event event = optionalEvent.get();
    EntityModel<Event> entityModel = EventResource.of(event).add(getProfileLink("resources-events-get"));
    if(event.getManager().equals(currentUser)) {
      entityModel.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
    }
    return ResponseEntity.ok(entityModel);
  }


  @PutMapping("/{id}")
  @Valid
  public ResponseEntity<?> updateEvent(@PathVariable Integer id,
                                       @RequestBody @Valid EventDto eventDto,
                                       @AuthenticationPrincipal(expression = "#this == 'anonymousUser'? null:account") Account currentUser,
                                       Errors errors) {
    if(errors.hasErrors()) {
      return badRequest(errors);
    }
    eventValidator.validate(eventDto,errors);

    if(errors.hasErrors()) {
      return badRequest(errors);
    }

    Optional<Event> optionalEvent = eventRepository.findById(id);
    if(optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Event beforeEvent = optionalEvent.get();
    Event updatedEvent = eventDtoToEntity(eventDto, beforeEvent, currentUser);
    Event savedEvent = eventRepository.save(updatedEvent);
    EntityModel<Event> entityModel = EventResource.of(savedEvent)
                                       .add(getProfileLink("resources-events-update"));
    return ResponseEntity.ok(entityModel);
  }

  private ResponseEntity<ErrorResource> badRequest(Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorResource(errors));
  }

  private Link getProfileLink(final String endPoint) {
    return Link.of("http://localhost:8080/docs/index.html#" + endPoint).withRel("profile");
  }

  private Event eventDtoToEntity(EventDto eventDto, Event event, Account account){
    Event newEvent = Event.builder()
                       .id(event.getId())
                       .name(eventDto.getName())
                       .description(eventDto.getDescription())
                       .beginEnrollmentDateTime(eventDto.getBeginEnrollmentDateTime())
                       .closeEnrollmentDateTime(eventDto.getCloseEnrollmentDateTime())
                       .beginEventDateTime(eventDto.getBeginEventDateTime())
                       .endEventDateTime(eventDto.getEndEventDateTime())
                       .location(eventDto.getLocation())
                       .basePrice(eventDto.getBasePrice())
                       .maxPrice(eventDto.getMaxPrice())
                       .manager(account)
                       .limitOfEnrollment(eventDto.getLimitOfEnrollment())
                       .build();
    newEvent.update();
    return newEvent;
  }


}
