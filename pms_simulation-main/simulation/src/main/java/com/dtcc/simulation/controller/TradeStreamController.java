package com.dtcc.simulation.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.dtcc.simulation.model.TradeEvent;
import com.dtcc.simulation.service.TradeGeneratorService;

@RestController
public class TradeStreamController {

    private final TradeGeneratorService generator;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TradeStreamController(TradeGeneratorService generator) {
        this.generator = generator;
    }

    @GetMapping(value = "/trade/stream", produces = "text/event-stream")
    public SseEmitter streamTrades() {

        SseEmitter emitter = new SseEmitter(0L);

        executor.submit(() -> {
            long lastHeartbeat = System.currentTimeMillis();

            try {
                while (true) {

                    TradeEvent event = generator.generateTrade();

                    emitter.send(SseEmitter.event()
                            .name("trade")
                            .data(event));

                    // 🔥 Invisible SSE heartbeat every 10 seconds
                    if (System.currentTimeMillis() - lastHeartbeat > 10000) {
                        emitter.send(":\n\n"); // <-- hidden keepalive
                        lastHeartbeat = System.currentTimeMillis();
                    }
                }

            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
