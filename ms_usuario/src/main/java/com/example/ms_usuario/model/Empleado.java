package com.example.ms_usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empleado", uniqueConstraints = {
        @UniqueConstraint(columnNames = "usuario_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean activo;
}