package com.example.ms_pedidos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import com.example.ms_pedidos.exception.ResourceNotFoundException;
import com.example.ms_pedidos.model.DetallePedido;
import com.example.ms_pedidos.model.Pedido;
import com.example.ms_pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        Pedido pedido = Pedido.builder()
                .cliente(request.getCliente())
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .detalles(request.getDetalles().stream()
                        .map(d -> DetallePedido.builder()
                                .producto(d.getProducto())
                                .cantidad(d.getCantidad())
                                .precioUnitario(d.getPrecioUnitario())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        pedido.getDetalles().forEach(d -> d.setPedido(pedido));

        return toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoResponseDTO obtenerPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        return toResponse(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .cliente(pedido.getCliente())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .detalles(pedido.getDetalles().stream()
                        .map(d -> DetallePedidoResponseDTO.builder()
                                .id(d.getId())
                                .producto(d.getProducto())
                                .cantidad(d.getCantidad())
                                .precioUnitario(d.getPrecioUnitario())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    @Override
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidoRepository.deleteById(id);
    }
     @Override
    public PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO request) {
        if (!id.equals(request.getId())) {
            throw new RuntimeException("El ID de la URL no coincide con el del cuerpo");
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setCliente(request.getCliente());
        pedido.setFecha(request.getFecha());
        pedido.setEstado(request.getEstado());

        Pedido actualizado = pedidoRepository.save(pedido);
       return PedidoResponseDTO.builder()
    .id(actualizado.getId())
    .cliente(actualizado.getCliente())
    .fecha(actualizado.getFecha())
    .estado(actualizado.getEstado())
    .detalles(actualizado.getDetalles().stream()
        .map(d -> new DetallePedidoResponseDTO(
            d.getId(),
            d.getProducto(),
            d.getCantidad(),
            d.getPrecioUnitario()
        ))
        .collect(Collectors.toList()))
      .build();
    }
}
