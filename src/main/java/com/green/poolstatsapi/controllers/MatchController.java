package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.Match;
import com.green.poolstatsapi.models.assemblers.MatchModelAssembler;
import com.green.poolstatsapi.repository.MatchRepository;
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
@RequestMapping(path="/matches")
public class MatchController {
    @Autowired
    private final MatchRepository repository;

    private final MatchModelAssembler assembler;

    Logger logger = LoggerFactory.getLogger(MatchController.class);


    MatchController(MatchRepository repository, MatchModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<Match>> all() {

        List<EntityModel<Match>> matches = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(matches, linkTo(methodOn(MatchController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewMatch (@RequestBody Match newMatch) {
        EntityModel<Match> entityModel = assembler.toModel(repository.save(newMatch));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<Match> one(@PathVariable Long id) {

        Match match = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("Venue", id));

        return assembler.toModel(match);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateMatch(@RequestBody Match newMatch, @PathVariable Long id) {
        Match updatedMatch = repository.findById(id)
                .map(match -> {
                    logger.info("In .map");
                    match.setFormat(newMatch.getFormat());
                    match.setGameType(newMatch.getGameType());
                    match.setRackType(newMatch.getRackType());
                    match.setTableType(newMatch.getTableType());
                    match.setVenue(newMatch.getVenue());
                    return repository.save(match);
                })
                .orElseGet(() -> {
                    logger.info("In ElseGet");
                    newMatch.setId(id);
                    return repository.save(newMatch);
                });
        EntityModel<Match> entityModel = assembler.toModel(updatedMatch);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
