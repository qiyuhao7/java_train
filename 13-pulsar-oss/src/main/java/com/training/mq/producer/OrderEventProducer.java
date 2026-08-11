package com.training.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 订单事件生产者
 */
@Service
@Slf4j
public class OrderEventProducer {

    private final PulsarClient client;
    private Producer<String> producer;

    @Value("${pulsar.topic:persistent://training/dev/order-events}")
    private String topic;

    public OrderEventProducer(PulsarClient client) {
        this.client = client;
    }

    @PostConstruct
    public void init() throws PulsarClientException {
        this.producer = client.newProducer(Schema.STRING)
            .topic(topic)
            .sendTimeout(5, TimeUnit.SECONDS)
            .blockIfQueueFull(true)
            .enableBatching(true)
            .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
            .create();
        log.info("Pulsar 生产者初始化完成: topic={}", topic);
    }

    /**
     * 同步发送
     */
    public String sendSync(String orderId, String eventJson) {
        try {
            MessageId msgId = producer.newMessage()
                .key(orderId)  // 分区 key：同订单到同分区，保证顺序
                .property("eventType", "ORDER_EVENT")
                .value(eventJson)
                .send();
            log.info("消息发送成功: orderId={}, msgId={}", orderId, msgId);
            return msgId.toString();
        } catch (PulsarClientException e) {
            log.error("消息发送失败: orderId={}", orderId, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     * 异步发送
     */
    public CompletableFuture<MessageId> sendAsync(String orderId, String eventJson) {
        return producer.newMessage()
            .key(orderId)
            .value(eventJson)
            .sendAsync()
            .whenComplete((msgId, ex) -> {
                if (ex != null) {
                    log.error("异步发送失败: orderId={}", orderId, ex);
                } else {
                    log.info("异步发送成功: orderId={}, msgId={}", orderId, msgId);
                }
            });
    }

    @PreDestroy
    public void close() throws PulsarClientException {
        if (producer != null) {
            producer.close();
            log.info("Pulsar 生产者已关闭");
        }
    }
}
