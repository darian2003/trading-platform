package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Holding;
import com.mobylab.springbackend.entity.Portfolio;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.AssetRepository;
import com.mobylab.springbackend.repository.HoldingRepository;
import com.mobylab.springbackend.repository.PortfolioRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.HoldingDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final AssetRepository assetRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository, HoldingRepository holdingRepository, AssetRepository assetRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.assetRepository = assetRepository;
    }

    public List<HoldingDto> getHoldings(Principal principal) {

        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(() -> new BadRequestException("User not found"));
        Portfolio portfolio = portfolioRepository.findByUserId(user.getId()).orElseThrow(() -> new BadRequestException("Portfolio not found"));
        List<Holding> holdingList = holdingRepository.findAllByPortfolioId(portfolio.getId());

        return holdingList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public HoldingDto deposit(String symbol, Double quantity, Principal principal) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive.");
        }

        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(() -> new BadRequestException("User not found"));
        Portfolio portfolio = portfolioRepository.findByUserId(user.getId()).orElseThrow(() -> new BadRequestException("Portfolio not found"));
        Asset asset = assetRepository.findBySymbol(symbol).orElseThrow(() -> new BadRequestException("Asset with symbol '" + symbol + "' does not exist."));
        Holding holding = holdingRepository.findByPortfolioAndAsset(portfolio, asset).orElseGet(() -> {
            Holding newHolding = new Holding();
            newHolding.setPortfolio(portfolio);
            newHolding.setAsset(asset);
            newHolding.setQuantity(0.0);
            newHolding.setReservedQuantity(0.0);
            return newHolding;
        });

        holding.setQuantity(holding.getQuantity() + quantity);
        holdingRepository.save(holding);

        return toDto(holding);
    }

    public HoldingDto withdraw(String symbol, Double quantity, Principal principal) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive.");
        }

        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(() -> new BadRequestException("User not found"));
        Portfolio portfolio = portfolioRepository.findByUserId(user.getId()).orElseThrow(() -> new BadRequestException("Portfolio not found"));
        Asset asset = assetRepository.findBySymbol(symbol).orElseThrow(() -> new BadRequestException("Asset with symbol '" + symbol + "' does not exist."));
        Holding holding = holdingRepository.findByPortfolioAndAsset(portfolio, asset).orElseThrow(() -> new BadRequestException("Holding not found"));

        double availableQuantity = holding.getQuantity() - holding.getReservedQuantity();

        if (availableQuantity < quantity) {
            throw new BadRequestException("Not enough quantity to withdraw.");
        }

        holding.setQuantity(holding.getQuantity() - quantity);
        if (holding.getQuantity() == 0 && holding.getReservedQuantity() == 0) {
            holdingRepository.delete(holding);
            return null;
        }

        holdingRepository.save(holding);
        return toDto(holding);
    }

    public HoldingDto toDto(Holding holding) {
        return new HoldingDto()
                .setAssetName(holding.getAsset().getName())
                .setAssetQuantity(holding.getQuantity())
                .setAssetSymbol(holding.getAsset().getSymbol())
                .setAssetPrice(holding.getAsset().getPrice())
                .setUpdatedAt(holding.getUpdatedAt())
                .setCreatedAt(holding.getCreatedAt())
                .setAssetReservedQuantity(holding.getReservedQuantity());
    }

}
