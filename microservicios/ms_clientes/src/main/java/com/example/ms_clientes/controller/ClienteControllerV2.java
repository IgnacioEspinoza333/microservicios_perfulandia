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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/clientes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clientes", description = "API para la gestión de clientes con soporte HATEOAS")
public class ClienteControllerV2 {

    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<ClienteResponseDto>> crearCliente(@Valid @RequestBody ClienteRequestDto dto) {
        log.info("Solicitud v2 para crear cliente con email: {}", dto.getEmail());

        ClienteResponseDto nuevoCliente = clienteService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(nuevoCliente.getId())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<ClienteResponseDto>> listarClientes() {
        log.debug("Solicitud v2 para listar clientes");

        List<EntityModel<ClienteResponseDto>> clientes = clienteService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteControllerV2.class).listarClientes()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene un cliente según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public EntityModel<ClienteResponseDto> obtenerClientePorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener cliente con id: {}", id);

        ClienteResponseDto cliente = clienteService.obtenerPorId(id);
        return assembler.toModel(cliente);
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<EntityModel<ClienteResponseDto>> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateDto dto) {

        log.info("Solicitud v2 para actualizar cliente con id: {}", id);

        ClienteResponseDto actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar cliente con id: {}", id);

        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}