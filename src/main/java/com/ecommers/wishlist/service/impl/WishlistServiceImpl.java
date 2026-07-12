package com.ecommers.wishlist.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.ecommers.wishlist.client.ProductClient;
import com.ecommers.wishlist.client.UserClient;
import com.ecommers.wishlist.dto.WishlistDto;
import com.ecommers.wishlist.model.Wishlist;
import com.ecommers.wishlist.repository.WishlistRepository;
import com.ecommers.wishlist.service.WishlistService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository repository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Override
    @Transactional
    public WishlistDto.WishlistResponse addToWishlist(WishlistDto.WishlistRequest request) {
        log.info("Agregando a wishlist userId={} productId={}", request.userId(), request.productId());
        try { userClient.getUserById(request.userId()); }
        catch (FeignException.NotFound e) { throw new IllegalArgumentException("Usuario no encontrado con id: " + request.userId()); }

        try { productClient.getProductById(request.productId()); }
        catch (FeignException.NotFound e) { throw new IllegalArgumentException("Producto no encontrado con id: " + request.productId()); }

        Wishlist entry = new Wishlist();
        entry.setUserId(request.userId());
        entry.setProductId(request.productId());
        return toResponse(repository.save(entry));
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long id) {
        log.info("Eliminando de wishlist id={}", id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Item de wishlist no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<WishlistDto.WishlistResponse> getWishlistByUser(Long userId) {
        log.info("Obteniendo wishlist userId={}", userId);
        return repository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public WishlistDto.UserDto getUserById(Long userId) {
        try { return userClient.getUserById(userId); }
        catch (FeignException.NotFound e) { throw new IllegalArgumentException("Usuario no encontrado con id: " + userId); }
    }

    private WishlistDto.WishlistResponse toResponse(Wishlist w) {
        return new WishlistDto.WishlistResponse(w.getId(), w.getUserId(), w.getProductId());
    }
}
