package com.ecommers.whitelist.repository;

import com.ecommers.whitelist.model.WhileList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhiteListRepository extends JpaRepository<WhileList, Long> {
    List<WhileList> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}