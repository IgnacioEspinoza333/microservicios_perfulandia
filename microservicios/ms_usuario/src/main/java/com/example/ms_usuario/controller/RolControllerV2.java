package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.RolModelAssembler;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;
import com.example.ms_usuario.service.RolService;
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
@RequestMapping("/api/v2/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "API para la gestión de roles con soporte HATEOAS")
public class RolControllerV2 {

    private final RolService rolService;
    private final RolModelAssembler assembler;

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar roles", description = "Obtiene todos los roles registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<RolResponseDto>> listar() {
        List<EntityModel<RolResponseDto>> roles = rolService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener rol por ID", description = "Obtiene un rol según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public EntityModel<RolResponseDto> obtenerPorId(@PathVariable Long id) {
        RolResponseDto rol = rolService.obtenerPorId(id);
        return assembler.toModel(rol);
    }

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear rol", description = "Registra un nuevo rol en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<RolResponseDto>> crear(
            @Valid @RequestBody RolRequestDto dto) {

        RolResponseDto nuevoRol = rolService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(RolControllerV2.class)
                        .obtenerPorId(nuevoRol.getId())).toUri())
                .body(assembler.toModel(nuevoRol));
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<EntityModel<RolResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequestDto dto) {

        RolResponseDto actualizado = rolService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar rol", description = "Elimina un rol por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}