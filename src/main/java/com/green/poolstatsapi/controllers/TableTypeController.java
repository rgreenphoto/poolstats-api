package com.green.poolstatsapi.controllers;

import com.green.poolstatsapi.exceptions.ModelNotFoundException;
import com.green.poolstatsapi.models.TableType;
import com.green.poolstatsapi.models.assemblers.TableTypeModelAssembler;
import com.green.poolstatsapi.repository.TableTypeRepository;
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
@RequestMapping(path="/table-types/")
public class TableTypeController {
    @Autowired
    private final TableTypeRepository repository;

    private final TableTypeModelAssembler assembler;



    TableTypeController(TableTypeRepository repository, TableTypeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public CollectionModel<EntityModel<TableType>> all() {

        List<EntityModel<TableType>> table_types = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(table_types, linkTo(methodOn(TableTypeController.class).all()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    ResponseEntity<?> addNewTableType (@RequestBody TableType newTableType) {
        EntityModel<TableType> entityModel = assembler.toModel(repository.save(newTableType));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<TableType> one(@PathVariable Long id) {

        TableType table_type = repository.findById(id) //
                .orElseThrow(() -> new ModelNotFoundException("TableType", id));

        return assembler.toModel(table_type);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateTableType(@RequestBody TableType newTableType, @PathVariable Long id) {
        TableType updatedTableType = repository.findById(id)
                .map(table_type -> {
                    table_type.setName(newTableType.getName());
                    return repository.save(table_type);
                })
                .orElseGet(() -> {
                    newTableType.setId(id);
                    return repository.save(newTableType);
                });

        EntityModel<TableType> entityModel = assembler.toModel(updatedTableType);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
