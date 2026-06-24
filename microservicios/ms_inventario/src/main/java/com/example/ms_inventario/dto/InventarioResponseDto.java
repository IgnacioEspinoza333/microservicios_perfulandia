package com.example.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponseDto {
    private Long id;
    private Long productoId;
    private Integer stock;
    private Long version;
    private ProductoResumenExternoDto producto;
}