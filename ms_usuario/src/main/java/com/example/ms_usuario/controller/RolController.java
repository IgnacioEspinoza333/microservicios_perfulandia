package com.example.ms_usuario.controller;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;
import com.example.ms_usuario.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolResponseDto crear(@Valid @RequestBody RolRequestDto dto) {
        return rolService.crear(dto);
    }

    @GetMapping
    public List<RolResponseDto> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public RolResponseDto obtenerPorId(@PathVariable Long id) {
        return rolService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public RolResponseDto actualizar(@PathVariable Long id,
                                     @Valid @RequestBody RolRequestDto dto) {
        return rolService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        return rolService.eliminar(id);
    }
}