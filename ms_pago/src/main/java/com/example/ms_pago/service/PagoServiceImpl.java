package com.example.ms_pago.service;

import com.example.ms_pago.dto.PagoRequestDTO;
import com.example.ms_pago.dto.PagoResponseDTO;
import com.example.ms_pago.exception.PagoNotFoundException;
import com.example.ms_pago.modelo.Pago;
import com.example.ms_pago.repository.PagoRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService  {
   private final PagoRepository pagoRepository;

    @Override
    public PagoResponseDTO crearPago(PagoRequestDTO request) {
        Pago pago = Pago.builder()
                .monto(request.getMonto())
                .metodo(request.getMetodo())
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();
        return toResponse(pagoRepository.save(pago));
    }

    @Override
    public PagoResponseDTO obtenerPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException("Pago no encontrado con id: " + id));
        return toResponse(pago);
    }

    @Override
    public List<PagoResponseDTO> listarPagos() {
        return pagoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new PagoNotFoundException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }

    private PagoResponseDTO toResponse(Pago pago) {
        return PagoResponseDTO.builder()
                .id(pago.getId())
                .monto(pago.getMonto())
                .metodo(pago.getMetodo())
                .fecha(pago.getFecha())
                .estado(pago.getEstado())
                .build();
    }
}
