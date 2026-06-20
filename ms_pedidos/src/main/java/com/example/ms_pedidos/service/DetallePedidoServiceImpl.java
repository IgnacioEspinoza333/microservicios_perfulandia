package com.example.ms_pedidos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ms_pedidos.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos.model.DetallePedido;
import com.example.ms_pedidos.repository.DetallePedidoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService  {
    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public DetallePedidoResponseDTO crearDetalle(DetallePedidoRequestDTO request) {
        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(request.getProducto());
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());

        DetallePedido guardado = detallePedidoRepository.save(detalle);
        return new DetallePedidoResponseDTO(
                guardado.getId(),
                guardado.getProducto(),
                guardado.getCantidad(),
                guardado.getPrecioUnitario()
        );
    }

    @Override
    public DetallePedidoResponseDTO obtenerDetalle(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        return new DetallePedidoResponseDTO(
                detalle.getId(),
                detalle.getProducto(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario()
        );
    }

    @Override
    public List<DetallePedidoResponseDTO> listarDetalles() {
        return detallePedidoRepository.findAll().stream()
                .map(d -> new DetallePedidoResponseDTO(
                        d.getId(),
                        d.getProducto(),
                        d.getCantidad(),
                        d.getPrecioUnitario()))
                .collect(Collectors.toList());
    }

    @Override
    public DetallePedidoResponseDTO actualizarDetalle(Long id, DetallePedidoRequestDTO request) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        detalle.setProducto(request.getProducto());
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());

        DetallePedido actualizado = detallePedidoRepository.save(detalle);
        return new DetallePedidoResponseDTO(
                actualizado.getId(),
                actualizado.getProducto(),
                actualizado.getCantidad(),
                actualizado.getPrecioUnitario()
        );
    }

    @Override
    public void eliminarDetalle(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new RuntimeException("Detalle no encontrado");
        }
        detallePedidoRepository.deleteById(id);
    }
}
