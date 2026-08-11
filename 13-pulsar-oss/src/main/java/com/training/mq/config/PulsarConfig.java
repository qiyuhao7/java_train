package com.training.mq.config;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Pulsar 客户端配置
 */
@Configuration
public class PulsarConfig {

    @Bean(destroyMethod = "close")
    public PulsarClient pulsarClient(@Value("${pulsar.service-url}") String serviceUrl)
            throws PulsarClientException {
        return PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .connectionTimeout(10, TimeUnit.SECONDS)
            .build();
    }
}
