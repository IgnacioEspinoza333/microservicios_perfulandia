package com.example.ms_pago.service;

import java.util.List;
import com.example.ms_pago.dto.PagoRequestDTO;
import com.example.ms_pago.dto.PagoResponseDTO;

public interface PagoService {
   PagoResponseDTO crearPago(PagoRequestDTO request);
    PagoResponseDTO obtenerPago(Long id);
    List<PagoResponseDTO> listarPagos();
    void eliminarPago(Long id);
    PagoResponseDTO actualizarPago(Long id, PagoRequestDTO request);
}
