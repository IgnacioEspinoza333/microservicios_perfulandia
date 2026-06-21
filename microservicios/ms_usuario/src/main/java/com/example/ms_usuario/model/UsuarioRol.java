package com.example.ms_usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_rol",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "rol_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}
