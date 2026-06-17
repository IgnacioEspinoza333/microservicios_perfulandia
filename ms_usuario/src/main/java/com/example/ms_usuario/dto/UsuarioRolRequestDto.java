package com.example.ms_usuario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRolRequestDto {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El rolId es obligatorio")
    private Long rolId;
}