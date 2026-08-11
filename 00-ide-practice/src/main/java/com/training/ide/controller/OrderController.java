package com.training.ide.controller;

import com.training.ide.model.Order;
import com.training.ide.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/orders — 下单（body: {"userId":1,"amount":999.00}）
     */
    @PostMapping
    public Order create(@RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        return orderService.createOrder(userId, amount);
    }

    /**
     * POST /api/orders/{id}/pay — 支付
     */
    @PostMapping("/{id}/pay")
    public Order pay(@PathVariable Long id) {
        return orderService.pay(id);
    }

    /**
     * POST /api/orders/{id}/cancel — 取消
     */
    @PostMapping("/{id}/cancel")
    public Order cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }
}
