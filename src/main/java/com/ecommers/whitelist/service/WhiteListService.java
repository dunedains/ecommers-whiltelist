package com.ecommers.whitelist.service;

import com.ecommers.whitelist.dto.WhiteListDto;

import java.util.List;

public interface WhiteListService {
    WhiteListDto.WishListResponse addToWhitelist(WhiteListDto.WishListRequest request);
    void removeFromWhitelist(Long id);
    List<WhiteListDto.WishListResponse> getWhitelistByUser(Long userId);
    boolean isInWhitelist(Long userId, Long productId);
    WhiteListDto.UserDto getUserById(Long userId);
}