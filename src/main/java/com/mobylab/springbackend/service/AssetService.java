package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Asset;

import com.mobylab.springbackend.entity.Holding;
import com.mobylab.springbackend.entity.Order;
import com.mobylab.springbackend.entity.Portfolio;
import com.mobylab.springbackend.exception.ApiException;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ErrorCodes;
import com.mobylab.springbackend.repository.AssetRepository;
import com.mobylab.springbackend.repository.HoldingRepository;
import com.mobylab.springbackend.repository.OrderRepository;
import com.mobylab.springbackend.service.dto.AssetCreateDto;
import com.mobylab.springbackend.service.dto.AssetDto;
import com.mobylab.springbackend.service.dto.AssetPriceUpdateDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssetService {

    private final AssetRepository assetRepository;
    private final HoldingRepository holdingRepository;
    private final OrderRepository orderRepository;

    public AssetService(AssetRepository assetRepository, HoldingRepository holdingRepository, OrderRepository orderRepository) {
        this.assetRepository = assetRepository;
        this.holdingRepository = holdingRepository;
        this.orderRepository = orderRepository;
    }

    public List<AssetDto> getAll() {
        List<Asset> assetList = assetRepository.findAll();
        return assetList.stream()
                        .map(this::toDto).collect(Collectors.toList());
    }

    public AssetDto getAssetBySymbol(String symbol) {
        Asset asset = assetRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ApiException("Asset with symbol '" + symbol + "' does not exist.", ErrorCodes.ASSET_NOT_FOUND));
        return toDto(asset);
    }

    public List<AssetDto> getAssetByPrefix(String prefix) {
        List<Asset> assetList = assetRepository.findBySymbolStartingWith(prefix).orElseThrow(() -> new ApiException("There are no assets with prefix '" + prefix + "'", ErrorCodes.ASSET_NOT_FOUND));
        return assetList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AssetDto addAsset(AssetCreateDto assetCreateDto) {
        if (assetRepository.existsBySymbol(assetCreateDto.getSymbol())) {
            throw new ApiException("Symbol already exists", ErrorCodes.SYMBOL_ALREADY_EXISTS);
        }
        Asset asset = this.toEntity(assetCreateDto);
        assetRepository.save(asset);
        return toDto(asset);
    }

    public AssetDto updateAssetPrice(AssetPriceUpdateDto assetPriceUpdateDto) {
        Asset asset = assetRepository.findBySymbol(assetPriceUpdateDto.getSymbol())
                .orElseThrow(() -> new ApiException("Asset with symbol '" + assetPriceUpdateDto.getSymbol() + "' does not exist.", ErrorCodes.ASSET_NOT_FOUND));
        asset.setPrice(assetPriceUpdateDto.getPrice());
        assetRepository.save(asset);
        return toDto(asset);
    }

    public AssetDto updateAsset(Long id, AssetCreateDto dto) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Asset with id '" + id + "' does not exist.", ErrorCodes.ASSET_NOT_FOUND));
        asset.setPrice(dto.getPrice());
        asset.setName(dto.getName());
        asset.setSymbol(dto.getSymbol());
        assetRepository.save(asset);
        return toDto(asset);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Asset with id '" + id + "' does not exist.", ErrorCodes.ASSET_NOT_FOUND));

        List<Holding> holdings = holdingRepository.findAllByAsset(asset);
        holdingRepository.deleteAll(holdings);

        List<Order> orders = orderRepository.findAllByAsset(asset);
        orderRepository.deleteAll(orders);

        assetRepository.delete(asset);
    }

    public AssetDto toDto(Asset asset) {
        return new AssetDto()
                .setId(asset.getId())
                .setName(asset.getName())
                .setSymbol(asset.getSymbol())
                .setPrice(asset.getPrice());
    }

    public Asset toEntity(AssetCreateDto assetCreateDto) {
        Asset asset = new Asset();
        asset.setName(assetCreateDto.getName());
        asset.setSymbol(assetCreateDto.getSymbol());
        asset.setPrice(assetCreateDto.getPrice());
        return asset;
    }
}
