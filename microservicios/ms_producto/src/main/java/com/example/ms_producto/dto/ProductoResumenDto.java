package com.example.ms_producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResumenDto {
    private Long id;
    private String nombre;
    private String sku;
    private Boolean activo;
}