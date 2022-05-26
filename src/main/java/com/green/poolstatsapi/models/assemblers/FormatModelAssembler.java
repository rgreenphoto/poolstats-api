package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.FormatController;
import com.green.poolstatsapi.models.Format;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FormatModelAssembler implements RepresentationModelAssembler<Format, EntityModel<Format>> {

    @Override
    public EntityModel<Format> toModel(Format format) {

        return EntityModel.of(format, //
                linkTo(methodOn(FormatController.class).one(format.getId())).withSelfRel(),
                linkTo(methodOn(FormatController.class).all()).withRel("formats"));
    }
}
