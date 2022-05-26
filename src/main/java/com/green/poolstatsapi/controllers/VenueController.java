package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.Venue;
import com.green.poolstatsapi.models.assemblers.VenueModelAssembler;
import com.green.poolstatsapi.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(path="/venues")
public class VenueController {
    @Autowired
    private final VenueRepository repository;

    private final VenueModelAssembler assembler;

    Logger logger = LoggerFactory.getLogger(VenueController.class);


    VenueController(VenueRepository repository, VenueModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<Venue>> all() {

        List<EntityModel<Venue>> venues = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(venues, linkTo(methodOn(VenueController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewVenue (@RequestBody Venue newVenue) {
        EntityModel<Venue> entityModel = assembler.toModel(repository.save(newVenue));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<Venue> one(@PathVariable Long id) {

        Venue venue = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("Venue", id));

        return assembler.toModel(venue);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateVenue(@RequestBody Venue newVenue, @PathVariable Long id) {
        Venue updatedVenue = repository.findById(id)
                .map(venue -> {
                    venue.setName(newVenue.getName());
                    venue.setDescription(newVenue.getDescription());
                    return repository.save(venue);
                })
                .orElseGet(() -> {
                    newVenue.setId(id);
                    return repository.save(newVenue);
                });
        EntityModel<Venue> entityModel = assembler.toModel(updatedVenue);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
