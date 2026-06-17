package com.ecommers.whitelist.controller;

import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.service.WhiteListService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UserController {

    private final WhiteListService service;

    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<WhiteListDto.UserDto>> getUserById(@PathVariable Long userId) {
        WhiteListDto.UserDto user = service.getUserById(userId);
        return ResponseEntity.ok(EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(userId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getWishlistByUser(userId)).withRel("wishlist")));
    }

    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<CollectionModel<EntityModel<WhiteListDto.WishlistResponse>>> getWishlistByUser(@PathVariable Long userId) {
        List<EntityModel<WhiteListDto.WishlistResponse>> items = service.getWhitelistByUser(userId).stream()
                .map(w -> EntityModel.of(w,
                        linkTo(methodOn(UserController.class).getUserById(w.userId())).withRel("owner")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(UserController.class).getWishlistByUser(userId)).withSelfRel()));
    }
}
