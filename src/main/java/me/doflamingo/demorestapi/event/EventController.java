package me.doflamingo.demorestapi.event;

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
public class EventController {

  @PostMapping
  public ResponseEntity createEvents(@RequestBody Event event) {
    event.setId(100);
    URI uri = linkTo(EventController.class).slash("{id}").toUri();
    return ResponseEntity.created(uri).body(event);
  }

}
