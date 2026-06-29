package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.assembler.AbastecimientoModelAssembler;
import com.example.ms_proveedores.dto.AbastecimientoRequestDto;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import com.example.ms_proveedores.dto.AbastecimientoUpdateDto;
import com.example.ms_proveedores.service.AbastecimientoService;
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
@RequestMapping("/api/v2/abastecimientos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Abastecimientos", description = "API para la gestión de abastecimientos con soporte HATEOAS")
public class AbastecimientoControllerV2 {

    private final AbastecimientoService abastecimientoService;
    private final AbastecimientoModelAssembler assembler;

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear abastecimiento", description = "Registra un nuevo abastecimiento para un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Abastecimiento creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbastecimientoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<AbastecimientoResponseDto>> crear(@Valid @RequestBody AbastecimientoRequestDto dto) {
        log.info("Solicitud v2 para crear abastecimiento para proveedorId: {}", dto.getProveedorId());

        AbastecimientoResponseDto nuevoAbastecimiento = abastecimientoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(AbastecimientoControllerV2.class).obtenerPorId(nuevoAbastecimiento.getId())).toUri())
                .body(assembler.toModel(nuevoAbastecimiento));
    }

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar abastecimientos", description = "Obtiene todos los abastecimientos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<AbastecimientoResponseDto>> listar() {
        log.debug("Solicitud v2 para listar abastecimientos");

        List<EntityModel<AbastecimientoResponseDto>> abastecimientos = abastecimientoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                abastecimientos,
                linkTo(methodOn(AbastecimientoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener abastecimiento por ID", description = "Obtiene un abastecimiento según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abastecimiento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbastecimientoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Abastecimiento no encontrado")
    })
    public EntityModel<AbastecimientoResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener abastecimiento con id: {}", id);

        AbastecimientoResponseDto abastecimiento = abastecimientoService.obtenerPorId(id);
        return assembler.toModel(abastecimiento);
    }

    @GetMapping(value = "/proveedor/{proveedorId}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar abastecimientos por proveedor", description = "Obtiene los abastecimientos asociados a un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public CollectionModel<EntityModel<AbastecimientoResponseDto>> listarPorProveedor(@PathVariable Long proveedorId) {
        log.debug("Solicitud v2 para listar abastecimientos del proveedor con id: {}", proveedorId);

        List<EntityModel<AbastecimientoResponseDto>> abastecimientos = abastecimientoService.listarPorProveedor(proveedorId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                abastecimientos,
                linkTo(methodOn(AbastecimientoControllerV2.class).listarPorProveedor(proveedorId)).withSelfRel(),
                linkTo(methodOn(ProveedorControllerV2.class).obtenerPorId(proveedorId)).withRel("proveedor"),
                linkTo(methodOn(AbastecimientoControllerV2.class).listar()).withRel("abastecimientos")
        );
    }

    @GetMapping(value = "/producto/{productoId}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar abastecimientos por producto", description = "Obtiene los abastecimientos asociados a un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public CollectionModel<EntityModel<AbastecimientoResponseDto>> listarPorProducto(@PathVariable Long productoId) {
        log.debug("Solicitud v2 para listar abastecimientos del producto con id: {}", productoId);

        List<EntityModel<AbastecimientoResponseDto>> abastecimientos = abastecimientoService.listarPorProducto(productoId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                abastecimientos,
                linkTo(methodOn(AbastecimientoControllerV2.class).listarPorProducto(productoId)).withSelfRel(),
                linkTo(methodOn(AbastecimientoControllerV2.class).listar()).withRel("abastecimientos")
        );
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar abastecimiento", description = "Actualiza un abastecimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abastecimiento actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbastecimientoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Abastecimiento no encontrado")
    })
    public ResponseEntity<EntityModel<AbastecimientoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AbastecimientoUpdateDto dto) {

        log.info("Solicitud v2 para actualizar abastecimiento con id: {}", id);

        AbastecimientoResponseDto actualizado = abastecimientoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar abastecimiento", description = "Elimina un abastecimiento por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Abastecimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Abastecimiento no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar abastecimiento con id: {}", id);

        abastecimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}