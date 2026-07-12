package com.ecommers.wishlist.controller;

import com.ecommers.wishlist.dto.WishlistDto;
import com.ecommers.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Lista de deseos; valida usuario y producto contra sus microservicios (Feign)")
public class WishlistController {

    private final WishlistService service;

    @PostMapping
    @Operation(summary = "Agregar un producto a la lista de deseos",
            description = "Valida que el usuario y el producto existan antes de guardar.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado a la wishlist"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el producto ya está en la lista"),
            @ApiResponse(responseCode = "404", description = "El usuario o el producto no existen")
    })
    public ResponseEntity<WishlistDto.WishlistResponse> addToWishlist(@Valid @RequestBody WishlistDto.WishlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addToWishlist(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Quitar un item de la lista de deseos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item eliminado"),
            @ApiResponse(responseCode = "404", description = "El item no existe")
    })
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long id) {
        service.removeFromWishlist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Ver la lista de deseos de un usuario")
    @ApiResponse(responseCode = "200", description = "Items de la wishlist (puede estar vacía)")
    public ResponseEntity<List<WishlistDto.WishlistResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getWishlistByUser(userId));
    }

    @GetMapping("/check")
    @Operation(summary = "Consultar si un producto está en la wishlist de un usuario")
    @ApiResponse(responseCode = "200", description = "true o false")
    public ResponseEntity<Boolean> isInWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        return ResponseEntity.ok(service.isInWishlist(userId, productId));
    }
}
