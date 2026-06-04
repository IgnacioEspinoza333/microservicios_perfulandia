package com.example.ms_usuario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmpleadoRequestDto {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}