package com.example.ms_producto.controller;

import com.example.ms_producto.assembler.ProductoModelAssembler;
import com.example.ms_producto.dto.ProductoRequestDto;
import com.example.ms_producto.dto.ProductoResponseDto;
import com.example.ms_producto.dto.ProductoUpdateDto;
import com.example.ms_producto.service.ProductoService;
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
@RequestMapping("/api/v2/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "API para la gestión de productos con soporte HATEOAS")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<ProductoResponseDto>> crear(@Valid @RequestBody ProductoRequestDto dto) {
        log.info("Solicitud v2 para crear producto con SKU: {}", dto.getSku());

        ProductoResponseDto nuevoProducto = productoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(nuevoProducto.getId())).toUri())
                .body(assembler.toModel(nuevoProducto));
    }

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<ProductoResponseDto>> listar() {
        log.debug("Solicitud v2 para listar productos");

        List<EntityModel<ProductoResponseDto>> productos = productoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                productos,
                linkTo(methodOn(ProductoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public EntityModel<ProductoResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener producto con id: {}", id);

        ProductoResponseDto producto = productoService.obtenerPorId(id);
        return assembler.toModel(producto);
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<ProductoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDto dto) {

        log.info("Solicitud v2 para actualizar producto con id: {}", id);

        ProductoResponseDto actualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar producto con id: {}", id);

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}