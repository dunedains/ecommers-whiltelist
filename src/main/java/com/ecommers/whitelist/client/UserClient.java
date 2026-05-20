package com.ecommers.whitelist.client;

import com.ecommers.whitelist.dto.WhiteListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.client.user_url}")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    WhiteListDto.UserDto getUserById(@PathVariable Long id);
}
