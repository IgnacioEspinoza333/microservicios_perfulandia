package com.example.ms_boleta.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boletas")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El número de la boleta es obligatorio")
    @Size(min = 5, max = 20, message = "El número debe tener entre 5 y 20 caracteres")
    private String numero;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del cliente debe tener entre 3 y 50 caracteres")
    private String cliente;


    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;


    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime fechaEmision;
}
