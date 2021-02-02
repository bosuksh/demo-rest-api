package me.doflamingo.demorestapi.error;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.doflamingo.demorestapi.index.IndexController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends RepresentationModel {


  @JsonUnwrapped
  public Errors errors;

  public ErrorResource(Errors errors) {
    this.errors = errors;
    add(linkTo(methodOn(IndexController.class).root()).withRel("index"));
  }

  public Errors getErrors() {
    return errors;
  }

//  private ErrorResource(){}
//
//  public static EntityModel<Errors> of(Errors errors) {
//    return EntityModel.of(errors).add(linkTo(methodOn(IndexController.class).root()).withRel("index"));
//
//  }
}
