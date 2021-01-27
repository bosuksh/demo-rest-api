package me.doflamingo.demorestapi.event.controller;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.event.repository.EventRepository;
import me.doflamingo.demorestapi.event.domain.Event;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

  private final EventRepository eventRepository;

  @PostMapping
  public ResponseEntity<?> createEvents(@RequestBody Event event) {
    Event newEvent = eventRepository.save(event);
    URI uri = linkTo(EventController.class).slash(newEvent).toUri();
    return ResponseEntity.created(uri).body(newEvent);
  }

}
