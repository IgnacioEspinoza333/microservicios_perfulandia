package com.example.ms_inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "inventarios",
    uniqueConstraints = @UniqueConstraint(name = "uk_inventario_producto", columnNames = "producto_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @Version
    private Long version;
}