package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Holding;
import com.mobylab.springbackend.entity.Transaction;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.AssetRepository;
import com.mobylab.springbackend.repository.HoldingRepository;
import com.mobylab.springbackend.repository.TransactionRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.HoldingDto;
import com.mobylab.springbackend.service.dto.TransactionDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getMyTransactions(Principal principal) {
        UUID userId = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new BadRequestException("User not found")).getId();
        return transactionRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto()
        .setId(transaction.getId())
        .setBuyOrderId(transaction.getBuyOrder().getId())
        .setSellOrderId(transaction.getSellOrder().getId())
        .setAssetSymbol(transaction.getAsset().getSymbol())
        .setQuantity(transaction.getQuantity())
        .setPrice(transaction.getPrice())
        .setTimestamp(transaction.getTimestamp());
    }


}
