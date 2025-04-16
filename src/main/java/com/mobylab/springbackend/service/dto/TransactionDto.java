package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;

public class TransactionDto {

    private Long id;
    private Long buyOrderId;
    private Long sellOrderId;
    private String assetSymbol;
    private Double price;
    private Double quantity;
    private LocalDateTime timestamp;

    public Double getQuantity() {
        return quantity;
    }

    public TransactionDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TransactionDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBuyOrderId() {
        return buyOrderId;
    }

    public TransactionDto setBuyOrderId(Long buyOrderId) {
        this.buyOrderId = buyOrderId;
        return this;
    }

    public Long getSellOrderId() {
        return sellOrderId;
    }

    public TransactionDto setSellOrderId(Long sellOrderId) {
        this.sellOrderId = sellOrderId;
        return this;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public TransactionDto setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public TransactionDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionDto setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
