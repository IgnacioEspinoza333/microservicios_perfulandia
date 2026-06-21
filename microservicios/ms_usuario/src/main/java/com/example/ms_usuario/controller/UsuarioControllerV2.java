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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
@RequiredArgsConstructor
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<UsuarioResponseDto>> listar() {
        List<EntityModel<UsuarioResponseDto>> usuarios = usuarioService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<UsuarioResponseDto> obtenerPorId(@PathVariable Long id) {
        UsuarioResponseDto usuario = usuarioService.obtenerPorId(id);
        return assembler.toModel(usuario);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioResponseDto>> crear(
            @Valid @RequestBody UsuarioRequestDto dto) {

        UsuarioResponseDto nuevoUsuario = usuarioService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(UsuarioControllerV2.class)
                        .obtenerPorId(nuevoUsuario.getId())).toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDto dto) {

        UsuarioResponseDto actualizado = usuarioService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/roles", produces = MediaTypes.HAL_JSON_VALUE)
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

    @DeleteMapping(value = "/{id}/roles/{rolId}", produces = MediaTypes.HAL_JSON_VALUE)
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

    @GetMapping(value = "/{id}/roles", produces = MediaTypes.HAL_JSON_VALUE)
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