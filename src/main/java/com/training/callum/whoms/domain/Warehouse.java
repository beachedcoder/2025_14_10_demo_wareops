package com.training.callum.whoms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity representing a warehouse for persistence.
 *
 * Mirrors WarehouseDTO fields plus a UUID primary key.
 */
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "warehouse_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @Column(nullable = false, name = "warehouse_phone")
    private String warehousePhone;

    @Column(nullable = false, name = "square_footage")
    private int squareFootage;

    @Column(nullable = false, name = "loading_docks")
    private int loadingDocks;

    // Default constructor for JPA
    public Warehouse() {}

    // Constructor for creating from DTO (without id, id generated separately)
    public Warehouse(final String name, final String address, final String city, final String state,
                     final String postalCode, final String warehousePhone, final int squareFootage,
                     final int loadingDocks) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.warehousePhone = warehousePhone;
        this.squareFootage = squareFootage;
        this.loadingDocks = loadingDocks;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getWarehousePhone() {
        return warehousePhone;
    }

    public void setWarehousePhone(final String warehousePhone) {
        this.warehousePhone = warehousePhone;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(final int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public int getLoadingDocks() {
        return loadingDocks;
    }

    public void setLoadingDocks(final int loadingDocks) {
        this.loadingDocks = loadingDocks;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse that)) {
            return false;
        }
        return squareFootage == that.squareFootage
                && loadingDocks == that.loadingDocks
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(address, that.address)
                && Objects.equals(city, that.city)
                && Objects.equals(state, that.state)
                && Objects.equals(postalCode, that.postalCode)
                && Objects.equals(warehousePhone, that.warehousePhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                address,
                city,
                state,
                postalCode,
                warehousePhone,
                squareFootage,
                loadingDocks);
    }
}