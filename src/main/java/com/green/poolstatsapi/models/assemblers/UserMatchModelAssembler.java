package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.UserMatchController;
import com.green.poolstatsapi.models.UserMatch;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserMatchModelAssembler implements RepresentationModelAssembler<UserMatch, EntityModel<UserMatch>> {

    @Override
    public EntityModel<UserMatch> toModel(UserMatch userMatch) {

        return EntityModel.of(userMatch, //
                linkTo(methodOn(UserMatchController.class).one(userMatch.getId())).withSelfRel(),
                linkTo(methodOn(UserMatchController.class).all()).withRel("user_matches"));
    }
}
