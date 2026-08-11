package com.training.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * 分布式锁工具
 * 基于 Redis SET NX EX + Lua 脚本释放
 */
@Component
public class DistributedLock {

    private final StringRedisTemplate redisTemplate;

    public DistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试加锁
     * @return 锁的 requestId（用于释放），null 表示加锁失败
     */
    public String tryLock(String lockKey, Duration timeout) {
        String requestId = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, requestId, timeout);
        return Boolean.TRUE.equals(success) ? requestId : null;
    }

    /**
     * 释放锁（Lua 脚本保证原子性：只释放自己的锁）
     */
    public boolean unlock(String lockKey, String requestId) {
        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "  return redis.call('del', KEYS[1]) " +
            "else " +
            "  return 0 " +
            "end";

        Long result = redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            List.of(lockKey),
            requestId
        );
        return result != null && result == 1L;
    }

    /**
     * 带重试的加锁
     */
    public String lockWithRetry(String lockKey, Duration timeout, int maxRetry, long retryIntervalMs) {
        for (int i = 0; i < maxRetry; i++) {
            String requestId = tryLock(lockKey, timeout);
            if (requestId != null) {
                return requestId;
            }
            try {
                Thread.sleep(retryIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }
}
