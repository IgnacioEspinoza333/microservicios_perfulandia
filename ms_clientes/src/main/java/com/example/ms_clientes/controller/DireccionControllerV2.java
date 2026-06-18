package com.example.ms_clientes.controller;

import com.example.ms_clientes.assembler.DireccionModelAssembler;
import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.dto.DireccionUpdateDto;
import com.example.ms_clientes.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
public class DireccionControllerV2 {

    private final DireccionService direccionService;
    private final DireccionModelAssembler assembler;

    @PostMapping(value = "/clientes/{clienteId}/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionResponseDto>> crear(
            @PathVariable Long clienteId,
            @Valid @RequestBody DireccionRequestDto dto) {

        log.info("Solicitud v2 para crear dirección al cliente con id: {}", clienteId);

        DireccionResponseDto nuevaDireccion = direccionService.crear(clienteId, dto);

        return ResponseEntity
                .created(linkTo(methodOn(DireccionControllerV2.class).obtenerPorId(nuevaDireccion.getId())).toUri())
                .body(assembler.toModel(nuevaDireccion));
    }

    @GetMapping(value = "/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<DireccionResponseDto>> listar() {
        log.debug("Solicitud v2 para listar direcciones");

        List<EntityModel<DireccionResponseDto>> direcciones = direccionService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/clientes/{clienteId}/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<DireccionResponseDto>> listarPorCliente(@PathVariable Long clienteId) {
        log.debug("Solicitud v2 para listar direcciones del cliente con id: {}", clienteId);

        List<EntityModel<DireccionResponseDto>> direcciones = direccionService.listarPorCliente(clienteId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).listarPorCliente(clienteId)).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(clienteId)).withRel("cliente"),
                linkTo(methodOn(DireccionControllerV2.class).listar()).withRel("direcciones")
        );
    }

    @GetMapping(value = "/direcciones/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<DireccionResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener dirección con id: {}", id);

        DireccionResponseDto direccion = direccionService.obtenerPorId(id);
        return assembler.toModel(direccion);
    }

    @PutMapping(value = "/direcciones/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DireccionUpdateDto dto) {

        log.info("Solicitud v2 para actualizar dirección con id: {}", id);

        DireccionResponseDto actualizada = direccionService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/direcciones/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar dirección con id: {}", id);

        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}