package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.service.AssetService;
import com.mobylab.springbackend.service.dto.AssetCreateDto;
import com.mobylab.springbackend.service.dto.AssetDto;
import com.mobylab.springbackend.service.dto.AssetPriceUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController implements SecuredRestController {

    private final AssetService assetService;

    public AssetController(AssetService assetService){
        this.assetService = assetService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AssetDto>> getAll(){
        return ResponseEntity.status(200).body(assetService.getAll());
    }

    @GetMapping("/getBySymbol/{symbol}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<AssetDto> getAssetsBySymbol(@PathVariable String symbol) {
        AssetDto assetDto = assetService.getAssetBySymbol(symbol);
        return ResponseEntity.ok(assetDto);
    }

    @GetMapping("/getByPrefix/{prefix}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AssetDto>> getAssetsByPrefix(@PathVariable String prefix){
        List<AssetDto> assetDtoList = assetService.getAssetByPrefix(prefix);
        return ResponseEntity.status(200).body(assetDtoList);
    }

    @PostMapping("/addAsset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AssetDto> addAsset(@RequestBody AssetCreateDto assetCreateDto) {
        AssetDto asset = assetService.addAsset(assetCreateDto);
        return ResponseEntity.status(201).body(asset);
    }

    @PatchMapping("/updateAssetPrice")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AssetDto> updateAssetPrice(@RequestBody AssetPriceUpdateDto assetPriceUpdateDto) {
        AssetDto updatedAsset = assetService.updateAssetPrice(assetPriceUpdateDto);
        return ResponseEntity.status(200).body(updatedAsset);
    }

    @PutMapping("/updateAsset/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AssetDto> updateAsset(@PathVariable Long id, @RequestBody AssetCreateDto assetCreateDto) {
        AssetDto updatedAsset = assetService.updateAsset(id, assetCreateDto);
        return ResponseEntity.status(200).body(updatedAsset);
    }

    @DeleteMapping("/deleteAsset/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
