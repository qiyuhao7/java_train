package com.training.ide.service;

import com.training.ide.model.Order;
import com.training.ide.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务
 * 练习点：
 * - 魔法数字（0.9 / 0.85）→ 用 Ctrl+Alt+C 提取常量
 * - 重复校验代码 → 用 Ctrl+Alt+M 提取方法
 * - 长表达式 → 用 Ctrl+Alt+V 提取变量
 */
@Service
public class UserService {

    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public UserService() {
        // 初始化示例数据
        for (int i = 1; i <= 5; i++) {
            User u = new User();
            u.setId((long) i);
            u.setUsername("user" + i);
            u.setEmail("user" + i + "@test.com");
            u.setAge(20 + i);
            u.setBalance(new BigDecimal("1000.00"));
            u.setVip(i % 2 == 0);
            u.setCreateTime(LocalDateTime.now());
            userStore.put(u.getId(), u);
        }
        idGen.set(5);
    }

    /**
     * 查询用户
     */
    public User getById(Long id) {
        return userStore.get(id);
    }

    /**
     * 查询所有用户
     */
    public List<User> listAll() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * 创建用户
     */
    public User create(User user) {
        // ⚠️ 练习点：这两段校验是重复代码，选中后用 Ctrl+Alt+M 提取成 checkUser 方法
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        user.setId(idGen.incrementAndGet());
        user.setBalance(new BigDecimal("0.00"));
        user.setCreateTime(LocalDateTime.now());
        userStore.put(user.getId(), user);
        return user;
    }

    /**
     * 计算实际支付金额（VIP 打折）
     * ⚠️ 练习点：0.9 和 0.85 是魔法数字，分别选中后 Ctrl+Alt+C 提取常量
     * ⚠️ 练习点：整个计算表达式很复杂，选中后 Ctrl+Alt+V 提取变量
     */
    public BigDecimal calculatePayAmount(Order order) {
        User user = userStore.get(order.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在: " + order.getUserId());
        }

        // VIP 用户 85 折，普通用户 9 折
        BigDecimal discount = user.getVip() ? new BigDecimal("0.85") : new BigDecimal("0.90");

        BigDecimal payAmount = order.getAmount()
                .multiply(discount)
                .setScale(2, RoundingMode.HALF_UP);
        return payAmount;
    }

    /**
     * 批量创建用户（练习 fori/iter 模板）
     */
    public List<User> batchCreate(int count) {
        List<User> result = new ArrayList<>();
        // 练习点：在这里打 fori 然后 Tab，看 IDEA 生成循环
        for (int i = 0; i < count; i++) {
            User u = new User();
            u.setUsername("batch" + (i + 1));
            u.setEmail("batch" + (i + 1) + "@test.com");
            u.setAge(18);
            u.setBalance(BigDecimal.ZERO);
            u.setVip(false);
            u.setCreateTime(LocalDateTime.now());
            u.setId(idGen.incrementAndGet());
            userStore.put(u.getId(), u);
            result.add(u);
        }
        return result;
    }
}
