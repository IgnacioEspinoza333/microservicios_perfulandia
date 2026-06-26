package com.example.ms_usuario.controller;

import com.example.ms_usuario.service.UsuarioRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuario-Rol", description = "API para la gestión de roles asignados a usuarios")
public class UsuarioRolController {

    private final UsuarioRolService usuarioRolService;

    @PostMapping("/{usuarioId}/roles/{rolId}")
    @Operation(summary = "Asignar rol a usuario", description = "Asigna un rol a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol asignado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
    })
    public ResponseEntity<String> asignarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        usuarioRolService.asignarRol(usuarioId, rolId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Rol asignado correctamente al usuario.");
    }

    @DeleteMapping("/{usuarioId}/roles/{rolId}")
    @Operation(summary = "Quitar rol de usuario", description = "Elimina un rol asignado a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol quitado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
    })
    public ResponseEntity<String> quitarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        usuarioRolService.quitarRol(usuarioId, rolId);
        return ResponseEntity.ok("Rol quitado correctamente del usuario.");
    }

    @GetMapping("/{usuarioId}/roles")
    @Operation(summary = "Listar roles de usuario", description = "Obtiene los roles asignados a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<List<String>> listarRolesDeUsuario(
            @PathVariable Long usuarioId
    ) {
        List<String> roles = usuarioRolService.listarRolesDeUsuario(usuarioId);
        return ResponseEntity.ok(roles);
    }
}