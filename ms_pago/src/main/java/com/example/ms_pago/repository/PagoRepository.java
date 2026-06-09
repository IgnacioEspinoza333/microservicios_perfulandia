package com.example.ms_pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_pago.modelo.Pago;

public interface PagoRepository  extends JpaRepository<Pago, Long>{

}
