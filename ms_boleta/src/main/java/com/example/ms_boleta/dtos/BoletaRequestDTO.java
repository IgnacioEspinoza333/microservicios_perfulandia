package com.example.ms_boleta.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoletaRequestDTO {
        private String numero;
    private String cliente;
    private Double monto;
}
