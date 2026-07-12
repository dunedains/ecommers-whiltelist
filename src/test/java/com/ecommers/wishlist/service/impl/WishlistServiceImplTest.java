package com.ecommers.wishlist.service.impl;

import com.ecommers.wishlist.client.ProductClient;
import com.ecommers.wishlist.client.UserClient;
import com.ecommers.wishlist.dto.WishlistDto.UserDto;
import com.ecommers.wishlist.dto.WishlistDto.WishlistRequest;
import com.ecommers.wishlist.dto.WishlistDto.WishlistResponse;
import com.ecommers.wishlist.model.Wishlist;
import com.ecommers.wishlist.repository.WishlistRepository;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la wishlist.
 * Se mockean el repositorio y los clientes de usuarios y productos (Feign).
 */
@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository repository;
    @Mock
    private ProductClient productClient;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private WishlistServiceImpl service;

    @Test
    @DisplayName("addToWishlist: si el usuario y el producto existen, guarda la entrada")
    void addToWishlist_usuarioYProductoExisten_guarda() {
        // Given: las validaciones remotas no lanzan (usuario y producto existen)
        when(repository.save(any(Wishlist.class))).thenAnswer(i -> {
            Wishlist w = i.getArgument(0);
            w.setId(1L);
            return w;
        });

        // When
        WishlistResponse response = service.addToWishlist(new WishlistRequest(2L, 10L));

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(2L);
        assertThat(response.productId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("addToWishlist: si el usuario no existe (404), lanza excepción y no guarda")
    void addToWishlist_usuarioInexistente_lanzaExcepcion() {
        // Given: el servicio de usuarios responde 404
        when(userClient.getUserById(2L)).thenThrow(mock(FeignException.NotFound.class));

        // When / Then
        assertThatThrownBy(() -> service.addToWishlist(new WishlistRequest(2L, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("addToWishlist: si el producto no existe (404), lanza excepción y no guarda")
    void addToWishlist_productoInexistente_lanzaExcepcion() {
        // Given: el usuario existe (no lanza) pero el producto responde 404
        when(productClient.getProductById(10L)).thenThrow(mock(FeignException.NotFound.class));

        // When / Then
        assertThatThrownBy(() -> service.addToWishlist(new WishlistRequest(2L, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Producto no encontrado");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("isInWishlist: refleja lo que devuelve el repositorio")
    void isInWishlist_devuelveExistencia() {
        when(repository.existsByUserIdAndProductId(2L, 10L)).thenReturn(true);

        assertThat(service.isInWishlist(2L, 10L)).isTrue();
    }

    @Test
    @DisplayName("getWishlistByUser: mapea las entradas del usuario")
    void getWishlistByUser_devuelveLista() {
        Wishlist w = new Wishlist();
        w.setId(1L);
        w.setUserId(2L);
        w.setProductId(10L);
        when(repository.findByUserId(2L)).thenReturn(java.util.List.of(w));

        assertThat(service.getWishlistByUser(2L)).hasSize(1);
    }

    @Test
    @DisplayName("removeFromWishlist: borra una entrada existente")
    void removeFromWishlist_existente_borra() {
        when(repository.existsById(1L)).thenReturn(true);

        service.removeFromWishlist(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("removeFromWishlist: si no existe, lanza excepción y no borra")
    void removeFromWishlist_inexistente_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.removeFromWishlist(99L))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("getUserById: devuelve el usuario validado vía Feign")
    void getUserById_devuelveUsuario() {
        when(userClient.getUserById(2L)).thenReturn(new UserDto(2L, "Ana", "ana@mail.com", "Calle 1"));

        UserDto user = service.getUserById(2L);

        assertThat(user.email()).isEqualTo("ana@mail.com");
    }
}
