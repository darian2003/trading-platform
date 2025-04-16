package com.mobylab.springbackend.service.dto;

public class AssetDto {

    private Long id;
    private String symbol;
    private String name;
    private Double price;

    // Getters and setters

    public AssetDto setName(String name) {
        this.name = name;
        return this;
    }

    public AssetDto setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Long getId() {
        return id;
    }

    public AssetDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public AssetDto setPrice(Double price) {
        this.price = price;
        return this;
    }
}
