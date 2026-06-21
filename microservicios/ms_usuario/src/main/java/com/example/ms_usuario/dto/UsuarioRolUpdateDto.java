package com.example.ms_usuario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRolUpdateDto {

    @NotNull(message = "El rolId es obligatorio")
    private Long rolId;
}