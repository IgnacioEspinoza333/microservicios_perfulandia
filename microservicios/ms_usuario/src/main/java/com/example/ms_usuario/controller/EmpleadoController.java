package com.example.ms_usuario.controller;

import com.example.ms_usuario.dto.EmpleadoRequestDto;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoResponseDto crear(@Valid @RequestBody EmpleadoRequestDto dto) {
        return empleadoService.crear(dto);
    }

    @GetMapping
    public List<EmpleadoResponseDto> listar() {
        return empleadoService.listar();
    }

    @GetMapping("/{id}")
    public EmpleadoResponseDto obtenerPorId(@PathVariable Long id) {
        return empleadoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public EmpleadoResponseDto actualizar(@PathVariable Long id,
                                          @Valid @RequestBody EmpleadoRequestDto dto) {
        return empleadoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        return empleadoService.eliminar(id);
    }
}