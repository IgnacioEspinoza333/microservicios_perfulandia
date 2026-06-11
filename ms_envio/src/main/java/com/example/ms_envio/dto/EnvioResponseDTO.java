package com.example.ms_envio.dto;

import java.time.LocalDateTime;

import com.example.ms_envio.model.EstadoEnvio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponseDTO {
    private Long id;
    private String direccionDestino;
    private String cliente;
    private LocalDateTime fechaEnvio;
    private EstadoEnvio estado;
}
