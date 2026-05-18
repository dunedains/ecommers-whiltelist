package com.ecommers.whitelist.client;

import com.ecommers.whitelist.dto.WhiteListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "${services.producto.url}")
public interface ProductoClient {

    @GetMapping("/api/productos/{id}")
    WhiteListDto.ProductDto getProductoById(@PathVariable("id") Long id);
}