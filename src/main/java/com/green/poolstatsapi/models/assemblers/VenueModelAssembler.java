package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.VenueController;
import com.green.poolstatsapi.models.Venue;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VenueModelAssembler implements RepresentationModelAssembler<Venue, EntityModel<Venue>> {

    @Override
    public EntityModel<Venue> toModel(Venue venue) {

        return EntityModel.of(venue, //
                linkTo(methodOn(VenueController.class).one(venue.getId())).withSelfRel(),
                linkTo(methodOn(VenueController.class).all()).withRel("venues"));
    }
}
