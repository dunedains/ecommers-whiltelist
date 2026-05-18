package com.ecommers.whitelist.dto;

public class WhiteListDto {
    public record WishListRequest(
            Long userId,
            Long productId
    ){}
    public record WishListResponse(
            Long id,
            Long userId,
            Long productId
    ){}
    public record ProductDto(
            Long id,
            String nombre,
            String descripcion,
            Double precio
    ){}
    public record UserDto(
            Long id,
            String name,
            String email,
            String direccion
    ){}
}

