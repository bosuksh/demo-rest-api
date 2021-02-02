package me.doflamingo.demorestapi.error;

import me.doflamingo.demorestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends EntityModel<Errors>{


  private ErrorResource(){}

  public static EntityModel<Errors> of(Errors errors) {
    return EntityModel.of(errors).add(linkTo(methodOn(IndexController.class).root()).withRel("index"));

  }
}
