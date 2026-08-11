package com.training.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用户缓存服务
 * 演示：Cache Aside、防穿透、防击穿、防雪崩
 */
@Service
public class UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_KEY = "user:%d";
    private static final String LOCK_KEY = "lock:user:%d";
    private static final int BASE_TTL_SECONDS = 7200;  // 2小时

    public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询用户（缓存优先 + 防穿透 + 防击穿）
     */
    public UserVO getById(Long id) {
        String key = String.format(USER_KEY, id);

        // 1. 查缓存
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            // 空值标记（防穿透）
            if (cached instanceof NullValue) {
                return null;
            }
            return (UserVO) cached;
        }

        // 2. 缓存未命中，加互斥锁防击穿
        String lockKey = String.format(LOCK_KEY, id);
        Boolean locked = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.TRUE.equals(locked)) {
            try {
                // 双重检查
                cached = redisTemplate.opsForValue().get(key);
                if (cached != null) {
                    return cached instanceof NullValue ? null : (UserVO) cached;
                }

                // 3. 查数据库
                UserVO user = loadFromDB(id);

                if (user == null) {
                    // 缓存空值防穿透（短 TTL）
                    redisTemplate.opsForValue().set(key, new NullValue(), Duration.ofMinutes(5));
                    return null;
                }

                // 4. 写缓存（随机 TTL 防雪崩）
                redisTemplate.opsForValue().set(key, user, randomTtl());
                return user;
            } finally {
                redisTemplate.delete(lockKey);
            }
        } else {
            // 未获取锁，短暂等待后重试
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            return getById(id);
        }
    }

    /**
     * 更新用户：先更新 DB，再删缓存
     */
    public void update(UserVO user) {
        // updateDB(user);  // 实际更新数据库
        String key = String.format(USER_KEY, user.getId());
        redisTemplate.delete(key);  // 删除而非更新
    }

    /**
     * 分布式计数器
     */
    public Long incrementView(Long articleId) {
        String key = "article:views:" + articleId;
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 随机 TTL（防雪崩：避免大量 key 同时过期）
     */
    private Duration randomTtl() {
        int offset = ThreadLocalRandom.current().nextInt(600);  // 0~10分钟随机
        return Duration.ofSeconds(BASE_TTL_SECONDS + offset);
    }

    private UserVO loadFromDB(Long id) {
        // 模拟数据库查询
        if (id > 0 && id <= 100) {
            return new UserVO(id, "用户" + id, "user" + id + "@test.com");
        }
        return null;
    }

    // ===== 内部类 =====

    static class NullValue implements java.io.Serializable {
    }
}
