package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.Format;
import com.green.poolstatsapi.models.assemblers.FormatModelAssembler;
import com.green.poolstatsapi.repository.FormatRepository;
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
@RequestMapping(path="/formats")
public class FormatController {
    @Autowired
    private final FormatRepository repository;

    private final FormatModelAssembler assembler;

    Logger logger = LoggerFactory.getLogger(FormatController.class);


    FormatController(FormatRepository repository, FormatModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<Format>> all() {

        List<EntityModel<Format>> formats = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(formats, linkTo(methodOn(FormatController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewFormat (@RequestBody Format newFormat) {
        EntityModel<Format> entityModel = assembler.toModel(repository.save(newFormat));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<Format> one(@PathVariable Long id) {

        Format format = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("Format", id));

        return assembler.toModel(format);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateFormat(@RequestBody Format newFormat, @PathVariable Long id) {
        Format updatedFormat = repository.findById(id)
                .map(format -> {
                    logger.info("In .map");
                    format.setName(newFormat.getName());
                    return repository.save(format);
                })
                .orElseGet(() -> {
                    logger.info("In ElseGet");
                    newFormat.setId(id);
                    return repository.save(newFormat);
                });
        EntityModel<Format> entityModel = assembler.toModel(updatedFormat);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
