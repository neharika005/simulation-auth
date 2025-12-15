package com.dtcc.simulation.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.dtcc.simulation.proto.TradeEventProto;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.Producer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RabbitStreamProducer {

    @Value("${app.rabbitmq.stream.port:5552}")
    private int PORT;

    @Value("${app.rabbitmq.stream.name:trade-stream}")
    private String STREAM_NAME;

    private  int MAX_RETRIES = 10;
    private  int RETRY_DELAY_MS = 3000;

    private Environment env;
    private Producer producer;

    @PostConstruct
    public void init() {
        String host = System.getenv("RABBITMQ_HOST");
        if (host == null || host.isBlank()) {
            host = "localhost";
        }

        env = createEnvironmentWithRetry(host);

        createStreamIfNotExists(env, STREAM_NAME);

        producer = env.producerBuilder()
                .stream(STREAM_NAME)
                .build();
    }

    private Environment createEnvironmentWithRetry(String host) {
        int attempts = 0;
        Exception lastException = null;

        while (attempts < MAX_RETRIES) {
            try {
                return Environment.builder()
                        .host(host)
                        .port(PORT)
                        .username("guest")
                        .password("guest")
                        .build();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Thread interrupted during retry", ex);
                }
            }
        }
        throw new IllegalStateException("Failed to connect to RabbitMQ Stream after retries", lastException);
    }

    private void createStreamIfNotExists(Environment env, String streamName) {
        try {
            env.streamCreator().stream(streamName).create();
        } catch (Exception e) {
            if (!e.getMessage().contains("STREAM_ALREADY_EXISTS")) {
                throw new IllegalStateException("Failed to create RabbitMQ Stream: " + streamName, e);
            }
        }
    }

    public void publish(TradeEventProto event) {
        if (producer == null) {
            throw new IllegalStateException("Producer not initialized");
        }

        if (event == null) {
            throw new IllegalArgumentException("TradeEvent cannot be null");
        }

        // PEEK THE MESSAGE BEFORE SENDING
        // log.info("PEEK RabbitMQ Stream Message: {}", event);

        Message msg = producer.messageBuilder()
                .addData(event.toByteArray())
                .build();

        producer.send(msg, confirmation -> {
            if (!confirmation.isConfirmed()) {
                throw new RuntimeException("RabbitMQ Stream failed to confirm message");
            }
        });
    }
}
