package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.UsuarioModelAssembler;
import com.example.ms_usuario.dto.AsignarRolRequestDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.UsuarioRequestDto;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import com.example.ms_usuario.dto.UsuarioUpdateDto;
import com.example.ms_usuario.service.UsuarioService;
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
@RequestMapping("/api/v2/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para la gestión de usuarios y asignación de roles con soporte HATEOAS")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<UsuarioResponseDto>> listar() {
        List<EntityModel<UsuarioResponseDto>> usuarios = usuarioService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public EntityModel<UsuarioResponseDto> obtenerPorId(@PathVariable Long id) {
        UsuarioResponseDto usuario = usuarioService.obtenerPorId(id);
        return assembler.toModel(usuario);
    }

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<UsuarioResponseDto>> crear(
            @Valid @RequestBody UsuarioRequestDto dto) {

        UsuarioResponseDto nuevoUsuario = usuarioService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(UsuarioControllerV2.class)
                        .obtenerPorId(nuevoUsuario.getId())).toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<UsuarioResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDto dto) {

        UsuarioResponseDto actualizado = usuarioService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/roles", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Asignar rol a usuario", description = "Asigna un rol a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol asignado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
    })
    public ResponseEntity<EntityModel<MessageResponseDto>> asignarRol(
            @PathVariable Long id,
            @Valid @RequestBody AsignarRolRequestDto dto) {

        MessageResponseDto response = usuarioService.asignarRol(id, dto);

        EntityModel<MessageResponseDto> model = EntityModel.of(
                response,
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(id)).withRel("usuario"),
                linkTo(methodOn(UsuarioControllerV2.class).listarRoles(id)).withRel("roles")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping(value = "/{id}/roles/{rolId}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Quitar rol de usuario", description = "Elimina un rol asignado a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
    })
    public ResponseEntity<EntityModel<MessageResponseDto>> quitarRol(
            @PathVariable Long id,
            @PathVariable Long rolId) {

        MessageResponseDto response = usuarioService.quitarRol(id, rolId);

        EntityModel<MessageResponseDto> model = EntityModel.of(
                response,
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(id)).withRel("usuario"),
                linkTo(methodOn(UsuarioControllerV2.class).listarRoles(id)).withRel("roles")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/{id}/roles", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar roles de usuario", description = "Obtiene todos los roles asignados a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public CollectionModel<EntityModel<String>> listarRoles(@PathVariable Long id) {
        List<EntityModel<String>> roles = usuarioService.listarRolesDeUsuario(id).stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        return CollectionModel.of(
                roles,
                linkTo(methodOn(UsuarioControllerV2.class).listarRoles(id)).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(id)).withRel("usuario")
        );
    }
}