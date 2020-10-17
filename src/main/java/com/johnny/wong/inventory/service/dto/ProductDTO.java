package com.johnny.wong.inventory.service.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;

    private String name;

    private String code;

    private BigDecimal weight;

    private Long totalStock;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String code, BigDecimal weight) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Long totalStock) {
        this.totalStock = totalStock;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", code='" + code + '\'' +
            ", weight=" + weight +
            ", totalStock=" + totalStock +
            '}';
    }
}
