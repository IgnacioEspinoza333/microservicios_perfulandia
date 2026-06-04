package com.example.ms_usuario.controller;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;
import com.example.ms_usuario.service.PermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permisos")
@RequiredArgsConstructor
public class PermisoController {

    private final PermisoService permisoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermisoResponseDto crear(@Valid @RequestBody PermisoRequestDto dto) {
        return permisoService.crear(dto);
    }

    @GetMapping
    public List<PermisoResponseDto> listar() {
        return permisoService.listar();
    }

    @GetMapping("/{id}")
    public PermisoResponseDto obtenerPorId(@PathVariable Long id) {
        return permisoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public PermisoResponseDto actualizar(@PathVariable Long id,
                                         @Valid @RequestBody PermisoRequestDto dto) {
        return permisoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        return permisoService.eliminar(id);
    }
}
