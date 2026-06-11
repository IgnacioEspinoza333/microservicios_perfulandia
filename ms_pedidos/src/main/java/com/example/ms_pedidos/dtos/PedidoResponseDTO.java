package com.example.ms_pedidos.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {
    private Long id;
    private String cliente;
    private LocalDateTime fecha;
    private String estado;
    private List<DetallePedidoResponseDTO> detalles;

}
