package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Holding;
import com.mobylab.springbackend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findAllByPortfolioId(Long portfolioId);
    Optional<Holding> findByPortfolioAndAsset(Portfolio portfolio, Asset asset);
    List<Holding> findAllByAsset(Asset asset);
}
