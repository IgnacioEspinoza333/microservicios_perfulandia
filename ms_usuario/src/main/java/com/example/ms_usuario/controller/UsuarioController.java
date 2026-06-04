package com.example.ms_usuario.controller;

import com.example.ms_usuario.dto.AsignarRolRequestDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.UsuarioRequestDto;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import com.example.ms_usuario.dto.UsuarioUpdateDto;
import com.example.ms_usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDto crear(@Valid @RequestBody UsuarioRequestDto dto) {
        return usuarioService.crear(dto);
    }

    @GetMapping
    public List<UsuarioResponseDto> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto actualizar(@PathVariable Long id,
                                         @Valid @RequestBody UsuarioUpdateDto dto) {
        return usuarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        return usuarioService.eliminar(id);
    }

    @PostMapping("/{id}/roles")
    public MessageResponseDto asignarRol(@PathVariable Long id,
                                         @Valid @RequestBody AsignarRolRequestDto dto) {
        return usuarioService.asignarRol(id, dto);
    }

    @DeleteMapping("/{id}/roles/{rolId}")
    public MessageResponseDto quitarRol(@PathVariable Long id,
                                        @PathVariable Long rolId) {
        return usuarioService.quitarRol(id, rolId);
    }

    @GetMapping("/{id}/roles")
    public List<String> listarRoles(@PathVariable Long id) {
        return usuarioService.listarRolesDeUsuario(id);
    }
}
