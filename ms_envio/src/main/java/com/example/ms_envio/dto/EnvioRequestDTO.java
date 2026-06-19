package com.example.ms_envio.dto;

import java.time.LocalDateTime;

import com.example.ms_envio.model.EstadoEnvio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioRequestDTO {
    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    private String direccionDestino;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del cliente debe tener entre 3 y 50 caracteres")
    private String cliente;

    @NotNull(message = "La fecha de envío es obligatoria")
    private LocalDateTime fechaEnvio;

    @NotNull(message = "El estado del envío es obligatorio")
    private EstadoEnvio estado;

    @NotNull(message = "El ID del envío es obligatorio en actualización")
    private Long id;
}

