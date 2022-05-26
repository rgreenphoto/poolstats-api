package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.RackType;
import com.green.poolstatsapi.models.assemblers.RackTypeModelAssembler;
import com.green.poolstatsapi.repository.RackTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/rack-types/")
public class RackTypeController {
    @Autowired
    private final RackTypeRepository repository;

    private final RackTypeModelAssembler assembler;



    RackTypeController(RackTypeRepository repository, RackTypeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<RackType>> all() {

        List<EntityModel<RackType>> rack_types = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rack_types, linkTo(methodOn(RackTypeController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewRackType (@RequestBody RackType newRackType) {
        EntityModel<RackType> entityModel = assembler.toModel(repository.save(newRackType));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<RackType> one(@PathVariable Long id) {

        RackType rack_type = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("RackType", id));

        return assembler.toModel(rack_type);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateRackType(@RequestBody RackType newRackType, @PathVariable Long id) {
        RackType updatedRackType = repository.findById(id)
                .map(rack_type -> {
                    rack_type.setName(newRackType.getName());
                    return repository.save(rack_type);
                })
                .orElseGet(() -> {
                    newRackType.setId(id);
                    return repository.save(newRackType);
                });

        EntityModel<RackType> entityModel = assembler.toModel(updatedRackType);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
