package com.mobylab.springbackend.service.dto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class OrderDto {

    private Long id;
    private String type;
    private Double price;
    private Double quantity;
    private Double remainingQuantity;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long assetId;
    private String assetSymbol;

    private Long userId;
    private String userEmail;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public OrderDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public OrderDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public OrderDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return type;
    }

    public OrderDto setType(String type) {
        this.type = type;
        return this;
    }

    public Long getAssetId() {
        return assetId;
    }

    public OrderDto setAssetId(Long assetId) {
        this.assetId = assetId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public OrderDto setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public OrderDto setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Double getRemainingQuantity() {
        return remainingQuantity;
    }
    public OrderDto setRemainingQuantity(Double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderDto setStatus(String status) {
        this.status = status;
        return this;
    }
}