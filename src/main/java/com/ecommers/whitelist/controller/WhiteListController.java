package com.ecommers.whitelist.controller;

import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.service.WhiteListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WhiteListController {

    private final WhiteListService service;

    @PostMapping
    public ResponseEntity<EntityModel<WhiteListDto.WishlistResponse>> addToWhitelist(@Valid @RequestBody WhiteListDto.WishlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(service.addToWhitelist(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWhitelist(@PathVariable Long id) {
        service.removeFromWhitelist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<WhiteListDto.WishlistResponse>>> getByUser(@PathVariable Long userId) {
        List<EntityModel<WhiteListDto.WishlistResponse>> items = service.getWhitelistByUser(userId).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(WhiteListController.class).getByUser(userId)).withSelfRel()));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isInWhitelist(@RequestParam Long userId, @RequestParam Long productId) {
        return ResponseEntity.ok(service.isInWhitelist(userId, productId));
    }

    private EntityModel<WhiteListDto.WishlistResponse> toModel(WhiteListDto.WishlistResponse item) {
        return EntityModel.of(item,
                linkTo(methodOn(WhiteListController.class).getByUser(item.userId())).withRel("user-wishlist"));
    }
}
