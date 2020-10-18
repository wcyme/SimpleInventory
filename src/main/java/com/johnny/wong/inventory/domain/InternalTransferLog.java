package com.johnny.wong.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A InternalTransferLog.
 */
@Entity
@Table(name = "internal_transfer_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InternalTransferLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "location_from")
    private String locationFrom;

    @Column(name = "location_to")
    private String locationTo;

    @Column(name = "quantity")
    private Long quantity;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @ManyToOne
    @JsonIgnoreProperties(value = "internalTransferLogs", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public InternalTransferLog productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public InternalTransferLog locationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
        return this;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public InternalTransferLog locationTo(String locationTo) {
        this.locationTo = locationTo;
        return this;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public Long getQuantity() {
        return quantity;
    }

    public InternalTransferLog quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public InternalTransferLog createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public InternalTransferLog user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternalTransferLog)) {
            return false;
        }
        return id != null && id.equals(((InternalTransferLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternalTransferLog{" +
            "id=" + getId() +
            ", productCode='" + getProductCode() + "'" +
            ", locationFrom='" + getLocationFrom() + "'" +
            ", locationTo='" + getLocationTo() + "'" +
            ", quantity=" + getQuantity() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
