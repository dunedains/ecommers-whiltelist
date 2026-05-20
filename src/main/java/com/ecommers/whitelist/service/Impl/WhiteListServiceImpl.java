package com.ecommers.whitelist.service.Impl;

import lombok.extern.slf4j.Slf4j;
import com.ecommers.whitelist.client.ProductClient;
import com.ecommers.whitelist.client.UserClient;
import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.model.Wishlist;
import com.ecommers.whitelist.repository.WhiteListRepository;
import com.ecommers.whitelist.service.WhiteListService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WhiteListServiceImpl implements WhiteListService {

    private final WhiteListRepository repository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Override
    @Transactional
    public WhiteListDto.WishlistResponse addToWhitelist(WhiteListDto.WishlistRequest request) {
        log.info("Agregando a wishlist userId={} productId={}", request.userId(), request.productId());
        try { userClient.getUserById(request.userId()); }
        catch (FeignException.NotFound e) { throw new RuntimeException("Usuario no encontrado con id: " + request.userId()); }

        try { productClient.getProductById(request.productId()); }
        catch (FeignException.NotFound e) { throw new RuntimeException("Producto no encontrado con id: " + request.productId()); }

        Wishlist entry = new Wishlist();
        entry.setUserId(request.userId());
        entry.setProductId(request.productId());
        return toResponse(repository.save(entry));
    }

    @Override
    @Transactional
    public void removeFromWhitelist(Long id) {
        log.info("Eliminando de wishlist id={}", id);
        repository.deleteById(id);
    }

    @Override
    public List<WhiteListDto.WishlistResponse> getWhitelistByUser(Long userId) {
        log.info("Obteniendo wishlist userId={}", userId);
        return repository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public boolean isInWhitelist(Long userId, Long productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public WhiteListDto.UserDto getUserById(Long userId) {
        try { return userClient.getUserById(userId); }
        catch (FeignException.NotFound e) { throw new RuntimeException("Usuario no encontrado con id: " + userId); }
    }

    private WhiteListDto.WishlistResponse toResponse(Wishlist w) {
        return new WhiteListDto.WishlistResponse(w.getId(), w.getUserId(), w.getProductId());
    }
}
