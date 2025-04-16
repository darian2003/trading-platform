package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;

public class HoldingDto {

    private String assetSymbol;
    private String assetName;
    private Double assetPrice;
    private Double assetQuantity;
    private Double assetReservedQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public HoldingDto setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
        return this;
    }

    public String getAssetName() {
        return assetName;
    }

    public HoldingDto setAssetName(String assetName) {
        this.assetName = assetName;
        return this;
    }

    public Double getAssetPrice() {
        return assetPrice;
    }

    public HoldingDto setAssetPrice(Double assetPrice) {
        this.assetPrice = assetPrice;
        return this;
    }

    public Double getAssetQuantity() {
        return assetQuantity;
    }

    public HoldingDto setAssetQuantity(Double assetQuantity) {
        this.assetQuantity = assetQuantity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public HoldingDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public HoldingDto setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Double getAssetReservedQuantity() {
        return assetReservedQuantity;
    }

    public HoldingDto setAssetReservedQuantity(Double assetReservedQuantity) {
        this.assetReservedQuantity = assetReservedQuantity;
        return this;
    }
}
