package com.example.ms_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DireccionRequestDto {

    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 2, max = 80, message = "La calle debe tener entre 2 y 80 caracteres")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Size(min = 1, max = 10, message = "El número debe tener entre 1 y 10 caracteres")
    private String numero;

    @Size(max = 50, message = "El depto no puede superar 50 caracteres")
    private String depto;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(min = 2, max = 60, message = "La comuna debe tener entre 2 y 60 caracteres")
    private String comuna;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 60, message = "La ciudad debe tener entre 2 y 60 caracteres")
    private String ciudad;

    @Size(max = 10, message = "El código postal no puede superar 10 caracteres")
    private String codigoPostal;

    @Size(max = 120, message = "La referencia no puede superar 120 caracteres")
    private String referencia;

    @NotNull(message = "El campo principal es obligatorio")
    private Boolean principal;
}