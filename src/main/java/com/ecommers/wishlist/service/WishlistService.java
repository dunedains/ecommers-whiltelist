package com.ecommers.wishlist.service;

import com.ecommers.wishlist.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    WishlistDto.WishlistResponse addToWishlist(WishlistDto.WishlistRequest request);
    void removeFromWishlist(Long id);
    List<WishlistDto.WishlistResponse> getWishlistByUser(Long userId);
    boolean isInWishlist(Long userId, Long productId);
    WishlistDto.UserDto getUserById(Long userId);
}
