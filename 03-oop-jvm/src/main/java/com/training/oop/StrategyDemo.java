package com.training.oop;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 策略模式练习：支付方式选择
 * 演示：接口多态的实际应用
 */
public class StrategyDemo {

    public static void main(String[] args) {
        var order = new Order("ORD-001", new BigDecimal("299.90"));

        // 根据用户选择动态切换策略
        Map<String, PaymentStrategy> strategies = Map.of(
            "wechat", new WechatPay(),
            "alipay", new AliPay(),
            "bank", new BankCardPay()
        );

        for (var entry : strategies.entrySet()) {
            System.out.println("=== 使用 " + entry.getKey() + " 支付 ===");
            var context = new PaymentContext(entry.getValue());
            context.pay(order);
            System.out.println();
        }
    }
}

// ===== 策略接口 =====
interface PaymentStrategy {
    String name();
    boolean pay(String orderId, BigDecimal amount);
    default void refund(String orderId, BigDecimal amount) {
        System.out.printf("  [%s] 退款: 订单=%s, 金额=%.2f%n", name(), orderId, amount);
    }
}

// ===== 具体策略 =====
class WechatPay implements PaymentStrategy {
    @Override
    public String name() { return "微信支付"; }

    @Override
    public boolean pay(String orderId, BigDecimal amount) {
        System.out.printf("  [%s] 调起微信扫码...%n", name());
        System.out.printf("  [%s] 支付成功: 订单=%s, 金额=%.2f%n", name(), orderId, amount);
        return true;
    }
}

class AliPay implements PaymentStrategy {
    @Override
    public String name() { return "支付宝"; }

    @Override
    public boolean pay(String orderId, BigDecimal amount) {
        System.out.printf("  [%s] 跳转支付宝页面...%n", name());
        System.out.printf("  [%s] 支付成功: 订单=%s, 金额=%.2f%n", name(), orderId, amount);
        return true;
    }
}

class BankCardPay implements PaymentStrategy {
    @Override
    public String name() { return "银行卡"; }

    @Override
    public boolean pay(String orderId, BigDecimal amount) {
        System.out.printf("  [%s] 验证银行卡信息...%n", name());
        System.out.printf("  [%s] 扣款成功: 订单=%s, 金额=%.2f%n", name(), orderId, amount);
        return true;
    }
}

// ===== 上下文：持有策略引用 =====
class PaymentContext {
    private final PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void pay(Order order) {
        boolean success = strategy.pay(order.getId(), order.getAmount());
        if (!success) {
            throw new RuntimeException("支付失败");
        }
    }
}

// ===== 订单 =====
class Order {
    private final String id;
    private final BigDecimal amount;

    public Order(String id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() { return id; }
    public BigDecimal getAmount() { return amount; }
}
