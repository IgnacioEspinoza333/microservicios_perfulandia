package com.example.ms_usuario.controller;

import com.example.ms_usuario.service.UsuarioRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioRolController {

    private final UsuarioRolService usuarioRolService;

    // Asignar un rol a un usuario
    @PostMapping("/{usuarioId}/roles/{rolId}")
    public ResponseEntity<String> asignarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        usuarioRolService.asignarRol(usuarioId, rolId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Rol asignado correctamente al usuario.");
    }

    // Quitar un rol a un usuario
    @DeleteMapping("/{usuarioId}/roles/{rolId}")
    public ResponseEntity<String> quitarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        usuarioRolService.quitarRol(usuarioId, rolId);
        return ResponseEntity.ok("Rol quitado correctamente del usuario.");
    }

    // Listar roles de un usuario
    @GetMapping("/{usuarioId}/roles")
    public ResponseEntity<List<String>> listarRolesDeUsuario(
            @PathVariable Long usuarioId
    ) {
        List<String> roles = usuarioRolService.listarRolesDeUsuario(usuarioId);
        return ResponseEntity.ok(roles);
    }
}
