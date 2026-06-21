package com.example.ms_usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolResponseDto {
    private Long id;
    private String nombre;
    private Set<String> permisos;
}