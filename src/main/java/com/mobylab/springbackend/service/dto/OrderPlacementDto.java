package com.mobylab.springbackend.service.dto;

public class OrderPlacementDto {

    private String assetSymbol;
    private Double price;
    private Double quantity;
    private String type;

    // Getters/setters

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public OrderPlacementDto setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public OrderPlacementDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public OrderPlacementDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return type;
    }

    public OrderPlacementDto setType(String type) {
        this.type = type;
        return this;
    }
}