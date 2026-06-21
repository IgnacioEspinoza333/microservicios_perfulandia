package com.example.ms_clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDto {
    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private Boolean activo;
    private Long version;
}