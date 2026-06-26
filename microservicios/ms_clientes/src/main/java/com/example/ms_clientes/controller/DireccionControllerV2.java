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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Direcciones", description = "API para la gestión de direcciones de clientes con soporte HATEOAS")
public class DireccionControllerV2 {

    private final DireccionService direccionService;
    private final DireccionModelAssembler assembler;

    @PostMapping(value = "/clientes/{clienteId}/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear dirección", description = "Registra una nueva dirección para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dirección creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<EntityModel<DireccionResponseDto>> crearDireccion(
            @PathVariable Long clienteId,
            @Valid @RequestBody DireccionRequestDto dto) {

        log.info("Solicitud v2 para crear dirección al cliente con id: {}", clienteId);

        DireccionResponseDto nuevaDireccion = direccionService.crear(clienteId, dto);

        return ResponseEntity
                .created(linkTo(methodOn(DireccionControllerV2.class).obtenerDireccionPorId(nuevaDireccion.getId())).toUri())
                .body(assembler.toModel(nuevaDireccion));
    }

    @GetMapping(value = "/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar direcciones", description = "Obtiene todas las direcciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<DireccionResponseDto>> listarDirecciones() {
        log.debug("Solicitud v2 para listar direcciones");

        List<EntityModel<DireccionResponseDto>> direcciones = direccionService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).listarDirecciones()).withSelfRel()
        );
    }

    @GetMapping(value = "/clientes/{clienteId}/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar direcciones por cliente", description = "Obtiene todas las direcciones asociadas a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public CollectionModel<EntityModel<DireccionResponseDto>> listarDireccionesPorCliente(@PathVariable Long clienteId) {
        log.debug("Solicitud v2 para listar direcciones del cliente con id: {}", clienteId);

        List<EntityModel<DireccionResponseDto>> direcciones = direccionService.listarPorCliente(clienteId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).listarDireccionesPorCliente(clienteId)).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(clienteId)).withRel("cliente"),
                linkTo(methodOn(DireccionControllerV2.class).listarDirecciones()).withRel("direcciones")
        );
    }

    @GetMapping(value = "/direcciones/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener dirección por ID", description = "Obtiene una dirección según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    public EntityModel<DireccionResponseDto> obtenerDireccionPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener dirección con id: {}", id);

        DireccionResponseDto direccion = direccionService.obtenerPorId(id);
        return assembler.toModel(direccion);
    }

    @PutMapping(value = "/direcciones/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar dirección", description = "Actualiza una dirección existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    public ResponseEntity<EntityModel<DireccionResponseDto>> actualizarDireccion(
            @PathVariable Long id,
            @Valid @RequestBody DireccionUpdateDto dto) {

        log.info("Solicitud v2 para actualizar dirección con id: {}", id);

        DireccionResponseDto actualizada = direccionService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping("/direcciones/{id}")
    @Operation(summary = "Eliminar dirección", description = "Elimina una dirección por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dirección eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar dirección con id: {}", id);

        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}