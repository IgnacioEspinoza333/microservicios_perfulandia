package com.example.ms_clientes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ms_clientes.dto.ClienteRequestDTO;
import com.example.ms_clientes.dto.ClienteResponseDTO;
import com.example.ms_clientes.assembler.ClienteModelAssembler;
import com.example.ms_clientes.service.ClienteService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v2/clientes")
@RequiredArgsConstructor
public class ClienteControllerV2 {
     private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ClienteResponseDTO>> listarTodos() {
        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).listarTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return assembler.toModel(cliente);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDTO>> crear(@Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO nuevoCliente = clienteService.crear(request);

        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).buscarPorId(nuevoCliente.getId())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO request) {

        ClienteResponseDTO actualizado = clienteService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
