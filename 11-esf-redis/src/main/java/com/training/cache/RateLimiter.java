package com.training.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * 滑动窗口限流器（基于 Redis ZSet）
 */
@Component
public class RateLimiter {

    private final StringRedisTemplate redisTemplate;

    public RateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 判断是否允许访问
     * @param key 限流标识（如 userId、IP）
     * @param maxRequests 窗口内最大请求数
     * @param windowSeconds 窗口大小（秒）
     * @return true=允许，false=限流
     */
    public boolean isAllowed(String key, int maxRequests, int windowSeconds) {
        String redisKey = "rate_limit:" + key;
        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000L);

        // 1. 移除窗口外的记录
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStart);

        // 2. 统计窗口内请求数
        Long count = redisTemplate.opsForZSet().zCard(redisKey);
        if (count != null && count >= maxRequests) {
            return false;  // 超出限制
        }

        // 3. 记录当前请求
        redisTemplate.opsForZSet().add(redisKey, UUID.randomUUID().toString(), now);

        // 4. 设置 key 过期时间（自动清理）
        redisTemplate.expire(redisKey, Duration.ofSeconds(windowSeconds * 2L));

        return true;
    }
}
