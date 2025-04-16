package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Transaction;
import com.mobylab.springbackend.service.TransactionService;
import com.mobylab.springbackend.service.dto.HoldingDto;
import com.mobylab.springbackend.service.dto.ModifyHoldingRequestDto;
import com.mobylab.springbackend.service.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController implements SecuredRestController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<TransactionDto>> getMyTransactions(Principal principal) {
        return ResponseEntity.ok(transactionService.getMyTransactions(principal));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

}
