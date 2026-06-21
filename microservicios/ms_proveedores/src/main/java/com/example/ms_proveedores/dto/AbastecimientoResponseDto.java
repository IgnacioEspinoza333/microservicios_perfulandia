package com.example.ms_proveedores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbastecimientoResponseDto {
    private Long id;
    private Long proveedorId;
    private String nombreProveedor;
    private Long productoId;
    private Integer cantidad;
    private String estado;
    private Instant fechaCreacion;
    private Long version;
}
