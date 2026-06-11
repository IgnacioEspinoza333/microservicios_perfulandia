package com.example.ms_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoResponseDTO {
    private Long id;
    private String producto;
    private Integer cantidad;
    private Double precioUnitario;

}
