package com.example.ms_clientes.controller;

import com.example.ms_clientes.assembler.ClienteModelAssembler;
import com.example.ms_clientes.dto.ClienteRequestDto;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.dto.ClienteUpdateDto;
import com.example.ms_clientes.service.ClienteService;
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
@RequestMapping("/api/v2/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteControllerV2 {

    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDto>> crear(@Valid @RequestBody ClienteRequestDto dto) {
        log.info("Solicitud v2 para crear cliente con email: {}", dto.getEmail());

        ClienteResponseDto nuevoCliente = clienteService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(nuevoCliente.getId())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ClienteResponseDto>> listar() {
        log.debug("Solicitud v2 para listar clientes");

        List<EntityModel<ClienteResponseDto>> clientes = clienteService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ClienteResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener cliente con id: {}", id);

        ClienteResponseDto cliente = clienteService.obtenerPorId(id);
        return assembler.toModel(cliente);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateDto dto) {

        log.info("Solicitud v2 para actualizar cliente con id: {}", id);

        ClienteResponseDto actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar cliente con id: {}", id);

        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}