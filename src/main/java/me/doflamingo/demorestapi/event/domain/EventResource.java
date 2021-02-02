package me.doflamingo.demorestapi.event.domain;


import me.doflamingo.demorestapi.event.controller.EventController;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event> {

  private EventResource() {}
  public static EntityModel<Event> of(Event event){
    return EntityModel.of(event).add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
  }


}
