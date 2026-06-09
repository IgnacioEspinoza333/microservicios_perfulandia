package com.example.ms_proveedores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbastecimientoRequestDto {

    @NotNull(message = "El proveedorId es obligatorio")
    private Long proveedorId;

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer cantidad;

    private String estado;
}
