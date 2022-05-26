package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.UserMatch;
import com.green.poolstatsapi.models.assemblers.UserMatchModelAssembler;
import com.green.poolstatsapi.repository.UserMatchRepository;
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
@RequestMapping(path="/user-matches")
public class UserMatchController {
    @Autowired
    private final UserMatchRepository repository;

    private final UserMatchModelAssembler assembler;

    Logger logger = LoggerFactory.getLogger(UserMatchController.class);


    UserMatchController(UserMatchRepository repository, UserMatchModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<UserMatch>> all() {

        List<EntityModel<UserMatch>> userMatches = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(userMatches, linkTo(methodOn(UserMatchController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewUserMatch (@RequestBody UserMatch newUserMatch) {
        EntityModel<UserMatch> entityModel = assembler.toModel(repository.save(newUserMatch));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public EntityModel<UserMatch> one(@PathVariable Long id) {

        UserMatch userMatch = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("UserMatch", id));

        return assembler.toModel(userMatch);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateUserMatch(@RequestBody UserMatch newUserMatch, @PathVariable Long id) {
        UserMatch updatedUserMatch = repository.findById(id)
                .map(userMatch -> {
                    userMatch.setMatch(newUserMatch.getMatch());
                    userMatch.setUser(newUserMatch.getUser());
                    userMatch.setFormatSkillLevel(newUserMatch.getFormatSkillLevel());
                    userMatch.setBreakRuns(newUserMatch.getBreakRuns());
                    userMatch.setFargoRating(newUserMatch.getFargoRating());
                    userMatch.setOtherRating(newUserMatch.getOtherRating());
                    userMatch.setHomeTeam(newUserMatch.getHomeTeam());
                    userMatch.setInnings(newUserMatch.getInnings());
                    userMatch.setMatchPoints(newUserMatch.getMatchPoints());
                    userMatch.setMatchScore(newUserMatch.getMatchScore());
                    userMatch.setOnSnaps(newUserMatch.getOnSnaps());
                    userMatch.setRace(newUserMatch.getRace());
                    userMatch.setRackless(newUserMatch.getRackless());
                    userMatch.setTableRuns(newUserMatch.getTableRuns());
                    return repository.save(userMatch);
                })
                .orElseGet(() -> {
                    logger.info("In ElseGet");
                    newUserMatch.setId(id);
                    return repository.save(newUserMatch);
                });
        EntityModel<UserMatch> entityModel = assembler.toModel(updatedUserMatch);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
