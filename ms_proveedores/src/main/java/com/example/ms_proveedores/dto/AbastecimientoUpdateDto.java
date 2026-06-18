package com.example.ms_proveedores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AbastecimientoUpdateDto {

    @NotNull(message = "El proveedorId es obligatorio")
    private Long proveedorId;


    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombreProveedor;

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer cantidad;

    @NotNull(message = "El estado es obligatorio")
    private String estado;
}
