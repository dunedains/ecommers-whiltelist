package com.ecommers.wishlist.controller;

import com.ecommers.wishlist.dto.WishlistDto;
import com.ecommers.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuario + Wishlist", description = "Vista combinada: datos del usuario (vía Feign al servicio de usuarios) junto a su lista de deseos")
public class UserController {

    private final WishlistService service;

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener los datos de un usuario",
            description = "Consulta el microservicio de usuarios mediante Feign.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "El usuario no existe")
    })
    public ResponseEntity<WishlistDto.UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }

    @GetMapping("/{userId}/wishlist")
    @Operation(summary = "Ver la lista de deseos de un usuario")
    @ApiResponse(responseCode = "200", description = "Items de la wishlist (puede estar vacía)")
    public ResponseEntity<List<WishlistDto.WishlistResponse>> getWishlistByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getWishlistByUser(userId));
    }
}
