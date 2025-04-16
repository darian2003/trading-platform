package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.PortfolioService;
import com.mobylab.springbackend.service.dto.HoldingDto;
import com.mobylab.springbackend.service.dto.ModifyHoldingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController implements SecuredRestController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<HoldingDto>> getMyHoldings(Principal principal) {
        return ResponseEntity.ok(portfolioService.getHoldings(principal));
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> depositAsset(@RequestBody ModifyHoldingRequestDto request, Principal principal) {
        portfolioService.deposit(request.getAssetSymbol(), request.getQuantity(), principal);
        return ResponseEntity.ok("Asset deposited.");
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> withdrawAsset(@RequestBody ModifyHoldingRequestDto request, Principal principal) {
        portfolioService.withdraw(request.getAssetSymbol(), request.getQuantity(), principal);
        return ResponseEntity.ok("Asset withdrawn.");
    }
}
