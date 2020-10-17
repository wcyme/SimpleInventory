package com.johnny.wong.inventory.repository;

import com.johnny.wong.inventory.domain.Stock;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByProduct_Code(String productCode);

    Optional<Stock> findOneByLocationAndProduct_Id(String location, Long productId);

    @Query("SELECT SUM(quantity) " +
        "FROM Stock " +
        "WHERE product_id IN "+
        "(SELECT id " +
        "FROM Product " +
        "WHERE code=?1)")
    Long sumOfQuantityByProductCode(String productCode);

}
