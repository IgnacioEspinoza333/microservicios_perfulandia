package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.assembler.ProveedorModelAssembler;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;
import com.example.ms_proveedores.service.ProveedorService;
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
@RequestMapping("/api/v2/proveedores")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Proveedores", description = "API para la gestión de proveedores con soporte HATEOAS")
public class ProveedorControllerV2 {

    private final ProveedorService proveedorService;
    private final ProveedorModelAssembler assembler;

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<ProveedorResponseDto>> crear(@Valid @RequestBody ProveedorRequestDto dto) {
        log.info("Solicitud v2 para crear proveedor con email: {}", dto.getEmail());

        ProveedorResponseDto nuevoProveedor = proveedorService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ProveedorControllerV2.class).obtenerPorId(nuevoProveedor.getId())).toUri())
                .body(assembler.toModel(nuevoProveedor));
    }

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar proveedores", description = "Obtiene todos los proveedores registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<ProveedorResponseDto>> listar() {
        log.debug("Solicitud v2 para listar proveedores");

        List<EntityModel<ProveedorResponseDto>> proveedores = proveedorService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                proveedores,
                linkTo(methodOn(ProveedorControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener proveedor por ID", description = "Obtiene un proveedor según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public EntityModel<ProveedorResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener proveedor con id: {}", id);

        ProveedorResponseDto proveedor = proveedorService.obtenerPorId(id);
        return assembler.toModel(proveedor);
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar proveedor", description = "Actualiza la información de un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<EntityModel<ProveedorResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorUpdateDto dto) {

        log.info("Solicitud v2 para actualizar proveedor con id: {}", id);

        ProveedorResponseDto actualizado = proveedorService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar proveedor con id: {}", id);

        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}