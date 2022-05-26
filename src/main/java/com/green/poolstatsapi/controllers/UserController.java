package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.User;
import com.green.poolstatsapi.models.assemblers.UserModelAssembler;
import com.green.poolstatsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private final UserRepository userRepository;

    private final UserModelAssembler assembler;

    UserController(UserRepository userRepository, UserModelAssembler assembler) {
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewUser (@RequestBody User newUser) {
        EntityModel<User> entityModel = assembler.toModel(userRepository.save(newUser));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<User>> all() {

        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> one(@PathVariable Long id) {

        User user = userRepository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("User", id));

        return assembler.toModel(user);
    }
}
