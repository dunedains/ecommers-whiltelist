package com.ecommers.wishlist.controller;

import com.ecommers.wishlist.dto.WishlistDto;
import com.ecommers.wishlist.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({WishlistController.class, UserController.class})
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishlistService service;

    @Test
    @DisplayName("POST /api/wishlist -> 201")
    void addToWishlist_devuelve201() throws Exception {
        when(service.addToWishlist(any())).thenReturn(new WishlistDto.WishlistResponse(1L, 2L, 10L));

        mockMvc.perform(post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":2,\"productId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/wishlist/user/{userId} -> 200")
    void getByUser_devuelve200() throws Exception {
        when(service.getWishlistByUser(2L)).thenReturn(List.of(new WishlistDto.WishlistResponse(1L, 2L, 10L)));

        mockMvc.perform(get("/api/wishlist/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(2));
    }

    @Test
    @DisplayName("GET /api/wishlist/check -> 200 con booleano")
    void check_devuelve200() throws Exception {
        when(service.isInWishlist(2L, 10L)).thenReturn(true);

        mockMvc.perform(get("/api/wishlist/check").param("userId", "2").param("productId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("DELETE /api/wishlist/{id} -> 204")
    void remove_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/wishlist/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/usuario/{userId} -> 200")
    void getUserById_devuelve200() throws Exception {
        when(service.getUserById(2L))
                .thenReturn(new WishlistDto.UserDto(2L, "Ana", "ana@mail.com", "Calle 1"));

        mockMvc.perform(get("/api/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@mail.com"));
    }
}
