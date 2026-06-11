package com.example.ms_pedidos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {
     private String cliente;
    private List<DetallePedidoRequestDTO> detalles;
}
