package com.ecommers.whitelist.controller;

import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.service.WhiteListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WhiteListController {

    private final WhiteListService service;

    @PostMapping
    public ResponseEntity<WhiteListDto.WishlistResponse> addToWhitelist(@Valid @RequestBody WhiteListDto.WishlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addToWhitelist(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWhitelist(@PathVariable Long id) {
        service.removeFromWhitelist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WhiteListDto.WishlistResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getWhitelistByUser(userId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isInWhitelist(@RequestParam Long userId, @RequestParam Long productId) {
        return ResponseEntity.ok(service.isInWhitelist(userId, productId));
    }
}
