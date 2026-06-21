package com.example.ms_producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDto {
    private Long id;
    private String nombre;
    private String sku;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}