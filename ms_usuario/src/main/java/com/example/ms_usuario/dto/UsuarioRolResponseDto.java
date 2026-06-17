package com.example.ms_usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRolResponseDto {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private Long rolId;
    private String nombreRol;
}