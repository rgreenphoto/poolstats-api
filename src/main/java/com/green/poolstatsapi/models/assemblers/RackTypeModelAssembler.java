package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.RackTypeController;
import com.green.poolstatsapi.models.RackType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RackTypeModelAssembler implements RepresentationModelAssembler<RackType, EntityModel<RackType>> {

    @Override
    public EntityModel<RackType> toModel(RackType racktype) {

        return EntityModel.of(racktype, //
                linkTo(methodOn(RackTypeController.class).one(racktype.getId())).withSelfRel(),
                linkTo(methodOn(RackTypeController.class).all()).withRel("rack_types"));
    }
}
