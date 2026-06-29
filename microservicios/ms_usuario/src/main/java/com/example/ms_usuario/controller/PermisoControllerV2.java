package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.PermisoModelAssembler;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;
import com.example.ms_usuario.service.PermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v2/permisos")
@RequiredArgsConstructor
@Tag(name = "Permisos", description = "API para la gestión de permisos con soporte HATEOAS")
public class PermisoControllerV2 {

    private final PermisoService permisoService;
    private final PermisoModelAssembler assembler;

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar permisos", description = "Obtiene todos los permisos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<PermisoResponseDto>> listar() {
        List<EntityModel<PermisoResponseDto>> permisos = permisoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                permisos,
                linkTo(methodOn(PermisoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener permiso por ID", description = "Obtiene un permiso según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PermisoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
    public EntityModel<PermisoResponseDto> obtenerPorId(@PathVariable Long id) {
        PermisoResponseDto permiso = permisoService.obtenerPorId(id);
        return assembler.toModel(permiso);
    }

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear permiso", description = "Registra un nuevo permiso en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permiso creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PermisoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<PermisoResponseDto>> crear(
            @Valid @RequestBody PermisoRequestDto dto) {

        PermisoResponseDto nuevoPermiso = permisoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(PermisoControllerV2.class)
                        .obtenerPorId(nuevoPermiso.getId())).toUri())
                .body(assembler.toModel(nuevoPermiso));
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar permiso", description = "Actualiza un permiso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PermisoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
    public ResponseEntity<EntityModel<PermisoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PermisoRequestDto dto) {

        PermisoResponseDto actualizado = permisoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar permiso", description = "Elimina un permiso por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        permisoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}