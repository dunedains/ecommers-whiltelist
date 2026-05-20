package com.ecommers.whitelist.repository;

import com.ecommers.whitelist.model.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhiteListRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}