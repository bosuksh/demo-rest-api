package me.doflamingo.demorestapi.index;

import me.doflamingo.demorestapi.events.controller.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

  @GetMapping("/api")
  public RepresentationModel root() {
    RepresentationModel representationModel = new RepresentationModel();
    representationModel.add(linkTo(EventController.class).withRel("events"));
    return representationModel;
  }
}
