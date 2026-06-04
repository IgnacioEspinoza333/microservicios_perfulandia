package com.example.ms_usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermisoRequestDto {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 80, message = "El código no puede superar 80 caracteres")
    private String codigo;
}
