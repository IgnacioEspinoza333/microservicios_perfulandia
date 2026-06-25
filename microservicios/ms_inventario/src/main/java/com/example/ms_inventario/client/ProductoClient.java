package com.example.ms_inventario.client;

import com.example.ms_inventario.config.FeignBasicAuthConfig;
import com.example.ms_inventario.dto.ProductoResumenExternoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-producto",
        configuration = FeignBasicAuthConfig.class
)
public interface ProductoClient {

    @GetMapping("/api/internal/productos/{id}")
    ProductoResumenExternoDto obtenerResumenPorId(@PathVariable("id") Long id);
}