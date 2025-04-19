package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.exception.ApiException;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ErrorCodes;
import com.mobylab.springbackend.exception.InternalServerErrorException;
import com.mobylab.springbackend.repository.*;
import com.mobylab.springbackend.service.dto.OrderModificationDto;
import com.mobylab.springbackend.service.dto.OrderPlacementDto;
import com.mobylab.springbackend.service.dto.OrderDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, HoldingRepository holdingRepository, PortfolioRepository portfolioRepository, AssetRepository assetRepository, TransactionRepository transactionRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }

    public List<OrderDto> getAll() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<OrderDto> getMyOrders(Principal principal) {
        List<Order> orderList = orderRepository.findByUserEmail(principal.getName());
        return orderList.stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public OrderDto placeBuyOrder(OrderPlacementDto dto, Principal principal) {

        if (dto.getQuantity() <= 0) {
            throw new ApiException("Quantity needs to be positive", ErrorCodes.INVALID_QUANTITY);
        }

        if (dto.getPrice() <= 0) {
            throw new ApiException("Price needs to be positive", ErrorCodes.INVALID_PRICE);
        }

        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ApiException("User not found", ErrorCodes.UNAUTHORIZED_ACCESS));
        Portfolio buyerPortfolio = portfolioRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.PORTFOLIO_NOT_FOUND));
        Asset asset = assetRepository.findBySymbol(dto.getAssetSymbol())
                .orElseThrow(() -> new ApiException("Asset not found", ErrorCodes.ASSET_NOT_FOUND));
        Asset usdAsset = assetRepository.findBySymbol("USD")
                .orElseThrow(() -> new ApiException("USD Asset not found", ErrorCodes.INTERNAL_ERROR));

        double reservedAmount = dto.getPrice() * dto.getQuantity();

        reserveAsset(usdAsset, buyerPortfolio, reservedAmount);

        List<Order> matchingOrders = orderRepository.findMatchingSellOrders(asset.getId(), dto.getPrice());
        Order myOrder = new Order(dto.getPrice(), dto.getQuantity(), Order.Type.BUY, user, asset);

        for (Order match : matchingOrders) {

            if (myOrder.getRemainingQuantity() == 0) {
                myOrder.complete();
                orderRepository.save(myOrder);
                break;
            }
            
            Portfolio sellerPortfolio = portfolioRepository.findByUserId(match.getUser().getId())
                    .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.ASSET_NOT_FOUND));

            Double quantity = Math.min(myOrder.getRemainingQuantity(), match.getRemainingQuantity());
            Double price = match.getPrice(); // Passive price wins

            match.fill(quantity);
            myOrder.fill(quantity);
            
            debitAsset(usdAsset, buyerPortfolio, price * quantity);
            creditAsset(asset, buyerPortfolio, quantity);

            creditAsset(usdAsset, sellerPortfolio, price * quantity);
            debitAsset(asset, sellerPortfolio, quantity);

            if (match.getRemainingQuantity() == 0) {
                match.complete();
            }

            orderRepository.save(myOrder);
            orderRepository.save(match);
            Transaction transaction = new Transaction(myOrder, match, asset, price, quantity);
            transactionRepository.save(transaction);
        }

        orderRepository.save(myOrder);
        emailService.sendTradeNotification(user.getEmail(), myOrder.getAsset().getSymbol(), myOrder.getQuantity(), myOrder.getPrice());
        return toDto(myOrder);
    }

    public OrderDto placeSellOrder(OrderPlacementDto dto, Principal principal) {

        if (dto.getQuantity() <= 0) {
            throw new ApiException("Quantity needs to be positive", ErrorCodes.INVALID_QUANTITY);
        }

        if (dto.getPrice() <= 0) {
            throw new ApiException("Price needs to be positive", ErrorCodes.INVALID_PRICE);
        }

        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ApiException("User not found", ErrorCodes.UNAUTHORIZED_ACCESS));
        Portfolio sellerPortfolio = portfolioRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.PORTFOLIO_NOT_FOUND));
        Asset asset = assetRepository.findBySymbol(dto.getAssetSymbol())
                .orElseThrow(() -> new ApiException("Asset not found", ErrorCodes.ASSET_NOT_FOUND));
        Asset usdAsset = assetRepository.findBySymbol("USD")
                .orElseThrow(() -> new ApiException("USD Asset not found", ErrorCodes.INTERNAL_ERROR));

        double reservedAmount = dto.getQuantity();

        reserveAsset(asset, sellerPortfolio, reservedAmount);

        List<Order> matchingOrders = orderRepository.findMatchingBuyOrders(asset.getId(), dto.getPrice());
        Order myOrder = new Order(dto.getPrice(), dto.getQuantity(), Order.Type.SELL, user, asset);
        orderRepository.save(myOrder);

        for (Order match : matchingOrders) {

            if (myOrder.getRemainingQuantity() == 0) {
                myOrder.complete();
                break;
            }

            Portfolio buyerPortfolio = portfolioRepository.findByUserId(match.getUser().getId())
                    .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.PORTFOLIO_NOT_FOUND));

            Double quantity = Math.min(myOrder.getRemainingQuantity(), match.getRemainingQuantity());
            Double price = match.getPrice(); // Passive price wins

            match.fill(quantity);
            myOrder.fill(quantity);

            debitAsset(usdAsset, buyerPortfolio, price * quantity);
            creditAsset(asset, buyerPortfolio, quantity);

            creditAsset(usdAsset, sellerPortfolio, price * quantity);
            debitAsset(asset, sellerPortfolio, quantity);

            if (match.getRemainingQuantity() == 0) {
                match.complete();
            }
            orderRepository.save(match);
            orderRepository.save(myOrder);

            Transaction transaction = new Transaction(myOrder, match, asset, price, quantity);
            transactionRepository.save(transaction);
        }

        orderRepository.save(myOrder);

        emailService.sendTradeNotification(user.getEmail(), myOrder.getAsset().getSymbol(), myOrder.getQuantity(), myOrder.getPrice());
        return toDto(myOrder);
    }

    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found", ErrorCodes.ORDER_CREATION_FAILED));
        return toDto(order);
    }

    public List<OrderDto> getActiveOrdersByAsset(String symbol) {
        Asset asset = assetRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ApiException("Asset not found", ErrorCodes.ASSET_NOT_FOUND));
        List<Order> orders = orderRepository.findAllByAssetAndStatus(asset, Order.Status.ACTIVE);
        return orders.stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public OrderDto modifyOrder(Long orderId, OrderModificationDto dto, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ApiException("User not found", ErrorCodes.UNAUTHORIZED_ACCESS));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found", ErrorCodes.ORDER_CREATION_FAILED));
        if (!order.getUser().getEmail().equals(principal.getName())) {
            throw new ApiException("You cannot modify this order", ErrorCodes.UNAUTHORIZED_ACCESS);
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new ApiException("Order already canceled", ErrorCodes.ORDER_CREATION_FAILED);
        }
        if (order.getStatus() == Order.Status.COMPLETED) {
            throw new ApiException("Order has already been completed", ErrorCodes.ORDER_CREATION_FAILED);
        }
        if (order.getFilledQuantity() >= dto.getNewQuantity()) {
            throw new ApiException("The order has already been filled for that quantity", ErrorCodes.ORDER_CREATION_FAILED);
        }

        Portfolio portfolio = portfolioRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.PORTFOLIO_NOT_FOUND));
        Asset asset = order.getType().equals(Order.Type.BUY)
                ? assetRepository.findBySymbol("USD").orElseThrow(() -> new InternalServerErrorException("USD Asset not found"))
                : assetRepository.findBySymbol(order.getAsset().getSymbol()).orElseThrow(() -> new InternalServerErrorException("Asset " + order.getAsset().getSymbol() + " not found"));

        Double reservedAmount = order.getRemainingQuantity();
        if (order.getType() == Order.Type.BUY) {
            reservedAmount *= order.getPrice();
            unreserveAsset(asset, portfolio, reservedAmount);
            reserveAsset(asset, portfolio, dto.getNewQuantity() * dto.getNewPrice());
        } else {
            unreserveAsset(asset, portfolio, reservedAmount);
            reserveAsset(asset, portfolio, dto.getNewQuantity());
        }

        order.setRemainingQuantity(dto.getNewQuantity() - order.getFilledQuantity());
        order.setPrice(dto.getNewPrice());
        order.setQuantity(dto.getNewQuantity());
        orderRepository.save(order);

        return toDto(order);
    }

    public OrderDto cancelOrder(Long orderId, Principal principal) {

        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new ApiException("User not found", ErrorCodes.UNAUTHORIZED_ACCESS));
        Order order = orderRepository.findById(orderId)

                .orElseThrow(() -> new ApiException("Order not found", ErrorCodes.ORDER_CREATION_FAILED));
        if (!order.getUser().getEmail().equals(principal.getName())) {
            throw new ApiException("You cannot cancel this order", ErrorCodes.UNAUTHORIZED_ACCESS);
        }
        if (order.getStatus().equals(Order.Status.COMPLETED)) {
            throw new ApiException("Order is already filled or empty.", ErrorCodes.ORDER_CREATION_FAILED);
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new ApiException("Order already canceled", ErrorCodes.ORDER_CREATION_FAILED);
        }

        Portfolio portfolio = portfolioRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("Portfolio not found", ErrorCodes.PORTFOLIO_NOT_FOUND));
        Asset asset = order.getType().equals(Order.Type.BUY)
                ? assetRepository.findBySymbol("USD").orElseThrow(() -> new InternalServerErrorException("USD Asset not found"))
                : assetRepository.findBySymbol(order.getAsset().getSymbol()).orElseThrow(() -> new InternalServerErrorException("Asset " + order.getAsset().getSymbol() + " not found"));

        if (order.getType() == Order.Type.BUY) {
            unreserveAsset(asset, portfolio, order.getPrice() * order.getRemainingQuantity());
        } else {
            unreserveAsset(asset, portfolio, order.getRemainingQuantity());
        }

        order.cancel();
        orderRepository.save(order);
        return toDto(order);
    }

    private void reserveAsset(Asset a, Portfolio p, double qty) {
        Holding h = holdingRepository.findByPortfolioAndAsset(p, a)
                .orElseThrow(() -> new ApiException("Asset not held", ErrorCodes.ORDER_CREATION_FAILED));
        if (h.getAvailableQuantity() < qty)
            throw new ApiException("Insufficient asset balance", ErrorCodes.ORDER_CREATION_FAILED);
        h.setReservedQuantity(h.getReservedQuantity() + qty);
        holdingRepository.save(h);
    }

    private void unreserveAsset(Asset a, Portfolio p, double qty) {
        Holding h = holdingRepository.findByPortfolioAndAsset(p, a)
                .orElseThrow(() -> new ApiException("Asset not held", ErrorCodes.ORDER_CREATION_FAILED));
        if (h.getReservedQuantity() < qty)
            throw new ApiException("Insufficient reserved asset balance", ErrorCodes.ORDER_CREATION_FAILED);
        h.setReservedQuantity(h.getReservedQuantity() - qty);
        holdingRepository.save(h);
    }

    private void debitAsset(Asset a, Portfolio p, double qty) {
        Holding h = holdingRepository.findByPortfolioAndAsset(p, a).orElseThrow(() -> new InternalServerErrorException("Asset not held"));
        h.setQuantity(h.getQuantity() - qty);
        h.setReservedQuantity(Math.max(0, h.getReservedQuantity() - qty));
        holdingRepository.save(h);
    }

    private void creditAsset(Asset a, Portfolio p, double qty) {
        Holding h = holdingRepository.findByPortfolioAndAsset(p, a)
                .orElseGet(() -> {
                    Holding newH = new Holding();
                    newH.setAsset(a);
                    newH.setPortfolio(p);
                    newH.setQuantity(0.0);
                    newH.setReservedQuantity(0.0);
                    return newH;
                });
        h.setQuantity(h.getQuantity() + qty);
        holdingRepository.save(h);
    }


    public OrderDto toDto(Order order) {
        return new OrderDto()
        .setId(order.getId())
        .setPrice(order.getPrice())
        .setQuantity(order.getQuantity())
        .setType(order.getType().name())
        .setAssetSymbol(order.getAsset().getSymbol())
        .setUserEmail(order.getUser().getEmail())
        .setCreatedAt(order.getCreatedAt())
        .setStatus(order.getStatus().name())
        .setRemainingQuantity(order.getRemainingQuantity());
    }

}
