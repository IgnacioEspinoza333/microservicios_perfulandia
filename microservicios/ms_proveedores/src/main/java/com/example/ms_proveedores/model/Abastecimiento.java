package com.example.ms_proveedores.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "abastecimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 20)
    private String estado; // PENDIENTE | COMPLETADO | CANCELADO

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Version
    private Long version;
}
