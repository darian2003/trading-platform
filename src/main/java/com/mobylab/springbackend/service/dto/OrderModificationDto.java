package com.mobylab.springbackend.service.dto;

public class OrderModificationDto {

    private Double newPrice;
    private Double newQuantity;

    // Getters and setters
    public Double getNewPrice() {
        return newPrice;
    }

    public OrderModificationDto setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
        return this;
    }

    public Double getNewQuantity() {
        return newQuantity;
    }

    public OrderModificationDto setNewQuantity(Double newQuantity) {
        this.newQuantity = newQuantity;
        return this;
    }
}
