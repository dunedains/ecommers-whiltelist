package com.ecommers.wishlist.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WishlistDto {

    public record WishlistRequest(
            @NotNull(message = "userId es obligatorio") Long userId,
            @NotNull(message = "productId es obligatorio") Long productId
    ) {}

    public record WishlistResponse(Long id, Long userId, Long productId) {}

    public record ProductDto(Long id, String name, String description, BigDecimal price) {}

    public record UserDto(Long id, String name, String email, String address) {}
}
