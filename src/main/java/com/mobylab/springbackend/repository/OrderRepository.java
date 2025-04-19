package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAll();
    List<Order> findByUserEmail(String email);
    List<Order> findAllByAsset(Asset asset);
    List<Order> findAllByAssetAndStatus(Asset asset, Order.Status status);

    @Query("""
    SELECT o FROM Order o
    WHERE o.type = 'SELL' AND o.asset.id = :assetId AND o.price <= :price
    ORDER BY o.price ASC, o.createdAt ASC
""")
    List<Order> findMatchingSellOrders(@Param("assetId") Long assetId, @Param("price") Double price);

    @Query("""
    SELECT o FROM Order o
    WHERE o.type = 'BUY' AND o.asset.id = :assetId AND o.price >= :price
    ORDER BY o.price DESC, o.createdAt ASC
""")
    List<Order> findMatchingBuyOrders(@Param("assetId") Long assetId, @Param("price") Double price);


}