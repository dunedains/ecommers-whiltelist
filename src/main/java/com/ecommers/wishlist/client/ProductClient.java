package com.ecommers.wishlist.client;

import com.ecommers.wishlist.dto.WishlistDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${feign.client.product_url}")
public interface ProductClient {

    @GetMapping("/api/productos/{id}")
    WishlistDto.ProductDto getProductById(@PathVariable Long id);
}
