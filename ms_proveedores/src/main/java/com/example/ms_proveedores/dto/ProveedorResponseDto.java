package com.example.ms_proveedores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorResponseDto {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private Long version;
}
