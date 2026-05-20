package com.ecommers.whitelist.client;

import com.ecommers.whitelist.dto.WhiteListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${feign.client.product_url}")
public interface ProductClient {

    @GetMapping("/api/productos/{id}")
    WhiteListDto.ProductDto getProductById(@PathVariable Long id);
}
