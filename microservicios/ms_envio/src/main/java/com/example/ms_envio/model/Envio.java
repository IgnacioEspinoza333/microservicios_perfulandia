package com.example.ms_envio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    private String direccionDestino;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del cliente debe tener entre 3 y 50 caracteres")
    private String cliente;

    @NotNull(message = "La fecha de envío es obligatoria")
    private LocalDateTime fechaEnvio;

    @NotNull(message = "El estado del envío es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;
}
