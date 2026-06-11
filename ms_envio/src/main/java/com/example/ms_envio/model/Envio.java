package com.example.ms_envio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "envios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envio {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccionDestino;

    private String cliente;

    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;
}
