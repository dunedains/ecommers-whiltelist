package com.ecommers.whitelist.service;

import com.ecommers.whitelist.dto.WhiteListDto;

import java.util.List;

public interface WhiteListService {
    WhiteListDto.WishlistResponse addToWhitelist(WhiteListDto.WishlistRequest request);
    void removeFromWhitelist(Long id);
    List<WhiteListDto.WishlistResponse> getWhitelistByUser(Long userId);
    boolean isInWhitelist(Long userId, Long productId);
    WhiteListDto.UserDto getUserById(Long userId);
}
