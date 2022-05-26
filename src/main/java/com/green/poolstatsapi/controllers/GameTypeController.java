package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.GameType;
import com.green.poolstatsapi.models.assemblers.GameTypeModelAssembler;
import com.green.poolstatsapi.repository.GameTypeRepository;
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
@RequestMapping(path="/game-types/")
public class GameTypeController {
    @Autowired
    private final GameTypeRepository repository;

    private final GameTypeModelAssembler assembler;



    GameTypeController(GameTypeRepository repository, GameTypeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<GameType>> all() {

        List<EntityModel<GameType>> game_types = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(game_types, linkTo(methodOn(GameTypeController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewGameType (@RequestBody GameType newGameType) {
        EntityModel<GameType> entityModel = assembler.toModel(repository.save(newGameType));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<GameType> one(@PathVariable Long id) {

        GameType game_type = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("GameType", id));

        return assembler.toModel(game_type);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateGameType(@RequestBody GameType newGameType, @PathVariable Long id) {
        GameType updatedGameType = repository.findById(id)
                .map(game_type -> {
                    game_type.setName(newGameType.getName());
                    return repository.save(game_type);
                })
                .orElseGet(() -> {
                    newGameType.setId(id);
                    return repository.save(newGameType);
                });

        EntityModel<GameType> entityModel = assembler.toModel(updatedGameType);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
