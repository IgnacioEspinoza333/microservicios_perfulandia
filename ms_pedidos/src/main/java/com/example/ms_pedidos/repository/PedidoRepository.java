package com.example.ms_pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_pedidos.modelo.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
