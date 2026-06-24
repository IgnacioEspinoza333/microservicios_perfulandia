package com.example.ms_inventario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoResumenExternoDto {
    private Long id;
    private String nombre;
    private String sku;
    private Boolean activo;
}