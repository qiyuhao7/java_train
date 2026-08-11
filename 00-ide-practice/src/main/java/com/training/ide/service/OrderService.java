package com.training.ide.service;

import com.training.ide.model.Order;
import com.training.ide.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单服务
 * 练习点：
 * - 接口实现跳转（Ctrl+Alt+B）
 * - 查找引用（Alt+F7）
 * - 断点调试（status 状态流转）
 */
@Service
public class OrderService {

    private final UserService userService;
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public OrderService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 下单（练习断点：在 status 相关行打断点）
     */
    public Order createOrder(Long userId, BigDecimal amount) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在: " + userId);
        }

        Order order = new Order();
        order.setId(idGen.incrementAndGet());
        order.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUserId(userId);
        order.setAmount(amount);
        order.setStatus(0);   // 待支付
        order.setCreateTime(LocalDateTime.now());
        orderStore.put(order.getId(), order);
        return order;
    }

    /**
     * 支付（练习条件断点：amount > 500 时才暂停）
     */
    public Order pay(Long orderId) {
        Order order = orderStore.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        if (order.getStatus() != 0) {
            throw new IllegalStateException("订单状态不允许支付: " + order.getStatus());
        }
        order.setStatus(1);   // 已支付
        return order;
    }

    /**
     * 取消订单
     */
    public Order cancel(Long orderId) {
        Order order = orderStore.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        order.setStatus(2);   // 已取消
        return order;
    }
}
