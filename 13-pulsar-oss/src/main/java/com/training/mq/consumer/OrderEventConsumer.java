package com.training.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 订单事件消费者
 * 演示：Shared 订阅、手动 ACK、死信队列、幂等消费
 */
@Service
@Slf4j
public class OrderEventConsumer {

    private final PulsarClient client;
    private final StringRedisTemplate redisTemplate;

    @Value("${pulsar.topic:persistent://training/dev/order-events}")
    private String topic;

    @Value("${pulsar.subscription:order-service-sub}")
    private String subscriptionName;

    public OrderEventConsumer(PulsarClient client, StringRedisTemplate redisTemplate) {
        this.client = client;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() throws PulsarClientException {
        client.newConsumer(Schema.STRING)
            .topic(topic)
            .subscriptionName(subscriptionName)
            .subscriptionType(SubscriptionType.Shared)  // 多实例负载均衡
            .ackTimeout(30, TimeUnit.SECONDS)           // 未确认超时重投
            .deadLetterPolicy(DeadLetterPolicy.builder()
                .maxRedeliverCount(3)                    // 最多重试 3 次
                .deadLetterTopic(topic + "-dlq")        // 死信队列
                .build())
            .messageListener(this::handleMessage)
            .subscribe();

        log.info("消费者启动: topic={}, subscription={}", topic, subscriptionName);
    }

    private void handleMessage(Consumer<String> consumer, Message<String> msg) {
        String eventId = msg.getProperty("eventId");
        String payload = msg.getValue();

        try {
            // 幂等检查：Redis 去重
            if (eventId != null && isDuplicate(eventId)) {
                log.info("重复消息，跳过: eventId={}", eventId);
                consumer.acknowledge(msg);
                return;
            }

            // 业务处理
            log.info("处理消息: msgId={}, key={}, payload={}",
                msg.getMessageId(), msg.getKey(), payload);
            processEvent(payload);

            // 标记已处理（幂等）
            if (eventId != null) {
                markProcessed(eventId);
            }

            // 确认消费
            consumer.acknowledge(msg);
        } catch (Exception e) {
            log.error("消息处理失败: msgId={}", msg.getMessageId(), e);
            // 不 ack → 自动重投（超过次数进入死信队列）
            consumer.negativeAcknowledge(msg);
        }
    }

    private void processEvent(String payload) {
        // 实际业务逻辑：发通知、更新状态等
        log.info("业务处理完成: {}", payload);
    }

    private boolean isDuplicate(String eventId) {
        String key = "msg:processed:" + eventId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private void markProcessed(String eventId) {
        String key = "msg:processed:" + eventId;
        redisTemplate.opsForValue().set(key, "1", Duration.ofHours(24));
    }
}
