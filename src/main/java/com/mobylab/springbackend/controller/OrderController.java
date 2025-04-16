package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Order;
import com.mobylab.springbackend.service.AssetService;
import com.mobylab.springbackend.service.OrderService;
import com.mobylab.springbackend.service.dto.AssetDto;
import com.mobylab.springbackend.service.dto.OrderDto;
import com.mobylab.springbackend.service.dto.OrderModificationDto;
import com.mobylab.springbackend.service.dto.OrderPlacementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController implements SecuredRestController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.status(200).body(orderService.getAll());
    }

    @GetMapping("/my")
    // @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDto>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal));
    }

    @PostMapping("/placeOrder")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderPlacementDto orderPlacementDto, Principal principal) {

        if (orderPlacementDto.getType().toUpperCase().equals(Order.Type.BUY.toString())) {
            return ResponseEntity.ok(orderService.placeBuyOrder(orderPlacementDto, principal));
        } else if (orderPlacementDto.getType().toUpperCase().equals(Order.Type.SELL.toString())) {
            return ResponseEntity.ok(orderService.placeSellOrder(orderPlacementDto, principal));
        } else {
            throw new IllegalArgumentException("Invalid order type: " + orderPlacementDto.getType());
        }
    }

    @GetMapping("/getOrder/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }

    @PatchMapping("/modifyOrder/{id}")
    // @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable Long id, @RequestBody OrderModificationDto orderModificationDto, Principal principal) {
        OrderDto orderDto = orderService.modifyOrder(id, orderModificationDto, principal);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("/cancelOrder/{id}")
    // @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(orderService.cancelOrder(id, principal));
    }
}
