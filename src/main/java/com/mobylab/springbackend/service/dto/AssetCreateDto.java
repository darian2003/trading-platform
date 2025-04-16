package com.mobylab.springbackend.service.dto;

public class AssetCreateDto {

    private String symbol;
    private String name;
    private Double price;

    // Getters and setters

    public AssetCreateDto setName(String name) {
        this.name = name;
        return this;
    }

    public AssetCreateDto setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

    public AssetCreateDto setPrice(Double price) {
        this.price = price;
        return this;
    }
}
