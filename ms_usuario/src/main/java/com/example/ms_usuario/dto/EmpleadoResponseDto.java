package com.example.ms_usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoResponseDto {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String emailUsuario;
    private Boolean activo;
}
