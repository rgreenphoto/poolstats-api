package com.green.poolstatsapi.models.assemblers;

import com.green.poolstatsapi.controllers.TableTypeController;
import com.green.poolstatsapi.models.TableType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TableTypeModelAssembler implements RepresentationModelAssembler<TableType, EntityModel<TableType>> {

    @Override
    public EntityModel<TableType> toModel(TableType tabletype) {

        return EntityModel.of(tabletype, //
                linkTo(methodOn(TableTypeController.class).one(tabletype.getId())).withSelfRel(),
                linkTo(methodOn(TableTypeController.class).all()).withRel("rack_types"));
    }
}
