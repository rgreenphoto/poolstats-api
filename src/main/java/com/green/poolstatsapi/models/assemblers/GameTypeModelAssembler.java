package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.GameTypeController;
import com.green.poolstatsapi.models.GameType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameTypeModelAssembler implements RepresentationModelAssembler<GameType, EntityModel<GameType>> {

    @Override
    public EntityModel<GameType> toModel(GameType gametype) {

        return EntityModel.of(gametype, //
                linkTo(methodOn(GameTypeController.class).one(gametype.getId())).withSelfRel(),
                linkTo(methodOn(GameTypeController.class).all()).withRel("game_types"));
    }
}
