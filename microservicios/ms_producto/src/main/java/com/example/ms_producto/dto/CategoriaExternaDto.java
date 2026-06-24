package com.example.ms_producto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaExternaDto {
    private Long id;
    private String nombre;
    private String descripcion;
}