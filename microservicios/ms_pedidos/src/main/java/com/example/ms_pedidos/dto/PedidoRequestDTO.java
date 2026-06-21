package com.example.ms_pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class PedidoRequestDTO {

    @NotNull(message = "El ID es obligatorio en actualización")
    private Long id;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del cliente debe tener entre 3 y 50 caracteres")
     private String cliente;

     @NotNull(message = "La fecha del pedido es obligatoria")
    private LocalDateTime fecha;


    @NotBlank(message = "El estado del pedido es obligatorio")
    private String estado;


    @NotEmpty(message = "Debe incluir al menos un detalle en el pedido")
    private List<DetallePedidoRequestDTO> detalles;
}
