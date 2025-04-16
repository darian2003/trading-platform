package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    boolean existsBySymbol(String symbol);
    List<Asset> findAll();
    Optional<List<Asset>> findBySymbolStartingWith(String prefix);
    Optional<Asset> findBySymbol(String symbol);

}
