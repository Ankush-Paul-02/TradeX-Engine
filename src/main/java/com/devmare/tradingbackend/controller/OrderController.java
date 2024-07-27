package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.CreateOrderReuqestDto;
import com.devmare.tradingbackend.business.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/pay")
    public ResponseEntity<DefaultResponse> payOrderPayment(
            @RequestBody CreateOrderReuqestDto createOrderReuqestDto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "orderId",
                                orderService.processOrder(
                                        createOrderReuqestDto.getCoinId(),
                                        createOrderReuqestDto.getOrderType(),
                                        createOrderReuqestDto.getQuantity()
                                )
                        ),
                        "Order payment successful"
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DefaultResponse> getOrderById(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "order",
                                orderService.getOrderById(orderId)
                        ),
                        "Order found successfully"
                )
        );
    }

    @GetMapping("/user-orders")
    public ResponseEntity<DefaultResponse> getAllOrdersByUserId(
            @RequestParam String orderType,
            @RequestParam(required = false) String assetSymbol
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "orders",
                                orderService.getAllOrdersByUserId(
                                        orderType,
                                        assetSymbol
                                )
                        ),
                        "Orders found successfully"
                )
        );
    }
}
