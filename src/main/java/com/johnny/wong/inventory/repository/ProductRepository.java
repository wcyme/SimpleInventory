package com.johnny.wong.inventory.repository;

import com.johnny.wong.inventory.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findOneByCode(String code);

}
