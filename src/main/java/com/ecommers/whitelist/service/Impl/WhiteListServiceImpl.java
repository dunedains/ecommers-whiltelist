package com.ecommers.whitelist.service.Impl;

import com.ecommers.whitelist.client.ProductoClient;
import com.ecommers.whitelist.client.UserClient;
import com.ecommers.whitelist.dto.WhiteListDto;
import com.ecommers.whitelist.model.WhileList;
import com.ecommers.whitelist.repository.WhiteListRepository;
import com.ecommers.whitelist.service.WhiteListService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WhiteListServiceImpl implements WhiteListService {

    private final WhiteListRepository repository;
    private final ProductoClient productoClient;
    private final UserClient userClient;

    @Override
    @Transactional
    public WhiteListDto.WishListResponse addToWhitelist(WhiteListDto.WishListRequest request) {
        try {
            userClient.getUserById(request.userId());
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado con id: " + request.userId());
        } catch (FeignException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de usuarios: " + e.getMessage());
        }

        try {
            productoClient.getProductoById(request.productId());
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Producto no encontrado con id: " + request.productId());
        } catch (FeignException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de productos: " + e.getMessage());
        }

        WhileList entry = new WhileList();
        entry.setUserId(request.userId());
        entry.setProductId(request.productId());

        return toResponse(repository.save(entry));
    }

    @Override
    @Transactional
    public void removeFromWhitelist(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<WhiteListDto.WishListResponse> getWhitelistByUser(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public boolean isInWhitelist(Long userId, Long productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public WhiteListDto.UserDto getUserById(Long userId) {
        try {
            return userClient.getUserById(userId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado con id: " + userId);
        } catch (FeignException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de usuarios: " + e.getMessage());
        }
    }

    private WhiteListDto.WishListResponse toResponse(WhileList entry) {
        return new WhiteListDto.WishListResponse(entry.getId(), entry.getUserId(), entry.getProductId());
    }
}
