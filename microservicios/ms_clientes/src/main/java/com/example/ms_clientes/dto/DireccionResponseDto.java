package com.example.ms_clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionResponseDto {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private String calle;
    private String numero;
    private String depto;
    private String comuna;
    private String ciudad;
    private String codigoPostal;
    private String referencia;
    private Boolean principal;
    private Long version;
}