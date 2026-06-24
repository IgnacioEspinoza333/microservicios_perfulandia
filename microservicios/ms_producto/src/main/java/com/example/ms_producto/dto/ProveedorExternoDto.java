package com.example.ms_producto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProveedorExternoDto {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private Long version;
}