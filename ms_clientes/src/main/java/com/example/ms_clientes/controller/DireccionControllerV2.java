package com.example.ms_clientes.controller;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_clientes.service.DireccionService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v2/direcciones")
@RequiredArgsConstructor
public class DireccionControllerV2 {
    private final DireccionService direccionService;
    private final DireccionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<DireccionResponseDTO>> listarTodas() {
        List<EntityModel<DireccionResponseDTO>> direcciones = direccionService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(direcciones,
                linkTo(methodOn(DireccionControllerV2.class).listarTodas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<DireccionResponseDTO> buscarPorId(@PathVariable Long id) {
        DireccionResponseDTO direccion = direccionService.buscarPorId(id);
        return assembler.toModel(direccion);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionResponseDTO>> crear(@Valid @RequestBody DireccionRequestDTO request) {
        DireccionResponseDTO nuevaDireccion = direccionService.crear(request);

        return ResponseEntity
                .created(linkTo(methodOn(DireccionControllerV2.class).buscarPorId(nuevaDireccion.getId())).toUri())
                .body(assembler.toModel(nuevaDireccion));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DireccionRequestDTO request) {

        DireccionResponseDTO actualizada = direccionService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
