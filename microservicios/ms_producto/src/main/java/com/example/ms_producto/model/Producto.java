package com.example.ms_producto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto", uniqueConstraints = {
        @UniqueConstraint(columnNames = "sku")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(nullable = false, unique = true, length = 80)
    private String sku;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @NotNull
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @NotNull(message = "La categoriaId es obligatoria")
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @NotNull(message = "La proveedorId es obligatoria")
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;
}