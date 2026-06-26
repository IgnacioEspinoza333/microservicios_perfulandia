package com.example.ms_inventario.controller;

import com.example.ms_inventario.assembler.InventarioModelAssembler;
import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.service.InventarioService;
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
@RequestMapping("/api/v2/inventarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventarios", description = "API para la gestión de inventarios con soporte HATEOAS")
public class InventarioControllerV2 {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear inventario", description = "Registra un nuevo inventario para un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<InventarioResponseDto>> crear(@Valid @RequestBody InventarioRequestDto dto) {
        log.info("Solicitud v2 para crear inventario para productoId: {}", dto.getProductoId());

        InventarioResponseDto nuevoInventario = inventarioService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(InventarioControllerV2.class).obtenerPorId(nuevoInventario.getId())).toUri())
                .body(assembler.toModel(nuevoInventario));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar inventarios", description = "Obtiene todos los inventarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<InventarioResponseDto>> listar() {
        log.debug("Solicitud v2 para listar inventarios");

        List<EntityModel<InventarioResponseDto>> inventarios = inventarioService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                inventarios,
                linkTo(methodOn(InventarioControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener inventario por ID", description = "Obtiene un inventario según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public EntityModel<InventarioResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener inventario con id: {}", id);

        InventarioResponseDto inventario = inventarioService.obtenerPorId(id);
        return assembler.toModel(inventario);
    }

    @GetMapping(value = "/producto/{productoId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener inventario por producto", description = "Obtiene el inventario asociado a un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public EntityModel<InventarioResponseDto> obtenerPorProductoId(@PathVariable Long productoId) {
        log.debug("Solicitud v2 para obtener inventario por productoId: {}", productoId);

        InventarioResponseDto inventario = inventarioService.obtenerPorProductoId(productoId);

        EntityModel<InventarioResponseDto> model = assembler.toModel(inventario);
        model.add(linkTo(methodOn(InventarioControllerV2.class).obtenerPorProductoId(productoId)).withSelfRel());

        return model;
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar inventario", description = "Actualiza un inventario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<EntityModel<InventarioResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioUpdateDto dto) {

        log.info("Solicitud v2 para actualizar inventario con id: {}", id);

        InventarioResponseDto actualizado = inventarioService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar inventario", description = "Elimina un inventario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar inventario con id: {}", id);

        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}