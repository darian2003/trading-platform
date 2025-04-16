package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", schema = "auth_server")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buy_order_id")
    private Order buyOrder;

    @ManyToOne
    @JoinColumn(name = "sell_order_id")
    private Order sellOrder;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private Double price;

    private Double quantity;

    private LocalDateTime timestamp = LocalDateTime.now();

    public Transaction(Order buyOrder, Order sellOrder, Asset asset, Double price, Double quantity) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.asset = asset;
        this.price = price;
        this.quantity = quantity;
    }

    public Transaction() {
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Getters and setters


    public Double getPrice() {
        return price;
    }

    public Asset getAsset() {
        return asset;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }
    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
