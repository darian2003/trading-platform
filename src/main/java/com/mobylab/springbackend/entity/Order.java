package com.mobylab.springbackend.entity;

import com.mobylab.springbackend.service.dto.OrderPlacementDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders", schema = "auth_server")
public class Order {

    public enum Type { BUY, SELL }
    public enum Status { ACTIVE, COMPLETED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private Double quantity;
    private Double remainingQuantity;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    public Order(){}

    public Order(Double price, Double quantity, Type type, User user, Asset asset) {
        this.price = price;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
        this.type = type;
        this.user = user;
        this.asset = asset;
    }


    public void fill(Double quantity) {
        this.remainingQuantity -= quantity;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public void complete() {
        this.status = Status.COMPLETED;
    }

    public Double getFilledQuantity() {
        return quantity - remainingQuantity;
    }

    public Long getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Type getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
