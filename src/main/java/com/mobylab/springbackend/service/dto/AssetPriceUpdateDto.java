package com.mobylab.springbackend.service.dto;

public class AssetPriceUpdateDto {

    private String symbol;
    private Double price;

    public Double getPrice() {
        return price;
    }

    public AssetPriceUpdateDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public AssetPriceUpdateDto setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

}
