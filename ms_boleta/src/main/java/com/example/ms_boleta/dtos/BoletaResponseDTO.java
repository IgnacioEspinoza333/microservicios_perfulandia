package com.example.ms_boleta.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoletaResponseDTO {
   private Long id;
    private String numero;
    private String cliente;
    private Double monto;
    private LocalDateTime fechaEmision;
}
