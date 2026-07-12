package com.ecommers.wishlist.client;

import com.ecommers.wishlist.dto.WishlistDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.client.user_url}")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    WishlistDto.UserDto getUserById(@PathVariable Long id);
}
