package com.ecommers.whitelist.controller;

import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.service.WhiteListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UserController {

    private final WhiteListService service;

    @GetMapping("/{userId}")
    public ResponseEntity<WhiteListDto.UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }

    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<List<WhiteListDto.WishListResponse>> getWishlistByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getWhitelistByUser(userId));
    }
}