package com.training.service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 账户服务（被测代码）
 */
public class AccountService {

    private final AccountRepository repo;
    private final NotificationClient notifier;

    public AccountService(AccountRepository repo, NotificationClient notifier) {
        this.repo = repo;
        this.notifier = notifier;
    }

    /**
     * 转账
     */
    public Account transfer(Long fromId, Long toId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("转账金额必须大于0");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("不能转账给自己");
        }

        Account from = repo.findById(fromId)
            .orElseThrow(() -> new NotFoundException("账户不存在: " + fromId));
        Account to = repo.findById(toId)
            .orElseThrow(() -> new NotFoundException("账户不存在: " + toId));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        repo.save(from);
        repo.save(to);
        notifier.send(to.getUserId(), "收到转账: ¥" + amount);
        return from;
    }

    /**
     * 查询余额
     */
    public BigDecimal getBalance(Long accountId) {
        return repo.findById(accountId)
            .map(Account::getBalance)
            .orElse(BigDecimal.ZERO);
    }
}

// ===== 依赖接口 =====

interface AccountRepository {
    Optional<Account> findById(Long id);
    Account save(Account account);
}

interface NotificationClient {
    void send(Long userId, String message);
}

// ===== 实体与异常 =====

class Account {
    private Long id;
    private Long userId;
    private BigDecimal balance;

    public Account(Long id, Long userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}

class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}

class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}
