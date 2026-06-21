package com.example.ms_usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
    private Long id;
    private String nombre;
    private String email;
    private Boolean activo;
    private List<String> roles;
    private Boolean esEmpleado;
}