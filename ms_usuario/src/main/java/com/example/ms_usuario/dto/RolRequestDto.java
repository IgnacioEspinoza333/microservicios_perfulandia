package com.example.ms_usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RolRequestDto {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 60, message = "El nombre del rol no puede superar 60 caracteres")
    private String nombre;

    private Set<Long> permisoIds;
}