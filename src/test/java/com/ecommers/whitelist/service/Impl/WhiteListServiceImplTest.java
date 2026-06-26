package com.ecommers.whitelist.service.Impl;

import com.ecommers.whitelist.client.ProductClient;
import com.ecommers.whitelist.client.UserClient;
import com.ecommers.whitelist.dto.WhiteListDto.UserDto;
import com.ecommers.whitelist.dto.WhiteListDto.WishlistRequest;
import com.ecommers.whitelist.dto.WhiteListDto.WishlistResponse;
import com.ecommers.whitelist.model.Wishlist;
import com.ecommers.whitelist.repository.WhiteListRepository;
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
class WhiteListServiceImplTest {

    @Mock
    private WhiteListRepository repository;
    @Mock
    private ProductClient productClient;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private WhiteListServiceImpl service;

    @Test
    @DisplayName("addToWhitelist: si el usuario y el producto existen, guarda la entrada")
    void addToWhitelist_usuarioYProductoExisten_guarda() {
        // Given: las validaciones remotas no lanzan (usuario y producto existen)
        when(repository.save(any(Wishlist.class))).thenAnswer(i -> {
            Wishlist w = i.getArgument(0);
            w.setId(1L);
            return w;
        });

        // When
        WishlistResponse response = service.addToWhitelist(new WishlistRequest(2L, 10L));

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(2L);
        assertThat(response.productId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("addToWhitelist: si el usuario no existe (404), lanza excepción y no guarda")
    void addToWhitelist_usuarioInexistente_lanzaExcepcion() {
        // Given: el servicio de usuarios responde 404
        when(userClient.getUserById(2L)).thenThrow(mock(FeignException.NotFound.class));

        // When / Then
        assertThatThrownBy(() -> service.addToWhitelist(new WishlistRequest(2L, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("addToWhitelist: si el producto no existe (404), lanza excepción y no guarda")
    void addToWhitelist_productoInexistente_lanzaExcepcion() {
        // Given: el usuario existe (no lanza) pero el producto responde 404
        when(productClient.getProductById(10L)).thenThrow(mock(FeignException.NotFound.class));

        // When / Then
        assertThatThrownBy(() -> service.addToWhitelist(new WishlistRequest(2L, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Producto no encontrado");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("isInWhitelist: refleja lo que devuelve el repositorio")
    void isInWhitelist_devuelveExistencia() {
        when(repository.existsByUserIdAndProductId(2L, 10L)).thenReturn(true);

        assertThat(service.isInWhitelist(2L, 10L)).isTrue();
    }

    @Test
    @DisplayName("getWhitelistByUser: mapea las entradas del usuario")
    void getWhitelistByUser_devuelveLista() {
        Wishlist w = new Wishlist();
        w.setId(1L);
        w.setUserId(2L);
        w.setProductId(10L);
        when(repository.findByUserId(2L)).thenReturn(java.util.List.of(w));

        assertThat(service.getWhitelistByUser(2L)).hasSize(1);
    }

    @Test
    @DisplayName("removeFromWhitelist: borra una entrada existente")
    void removeFromWhitelist_existente_borra() {
        when(repository.existsById(1L)).thenReturn(true);

        service.removeFromWhitelist(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("removeFromWhitelist: si no existe, lanza excepción y no borra")
    void removeFromWhitelist_inexistente_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.removeFromWhitelist(99L))
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
