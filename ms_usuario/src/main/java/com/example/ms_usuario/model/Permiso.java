package com.example.ms_usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permiso", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String codigo;
}
