package com.mobylab.springbackend.service.dto;

public class ModifyHoldingRequestDto {
    private String assetSymbol;
    private Double quantity;

    // Getters and setters

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public ModifyHoldingRequestDto setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public ModifyHoldingRequestDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }
}