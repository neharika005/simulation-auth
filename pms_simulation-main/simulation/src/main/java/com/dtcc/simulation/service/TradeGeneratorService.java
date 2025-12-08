package com.dtcc.simulation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dtcc.simulation.model.TradeEvent;

@Service
public class TradeGeneratorService {

    private final Random random = new Random();

    private enum Mode { NORMAL, HYPE, PAUSE }
    private Mode currentMode = Mode.NORMAL;
    private long modeEndTime = System.currentTimeMillis();

    private final List<UUID> pList = List.of(
        UUID.fromString("a3f1c720-9b71-11ef-8023-9fb6bfe1c001"),
        UUID.fromString("a3f1c721-9b71-11ef-8023-9fb6bfe1c002"),
        UUID.fromString("a3f1c722-9b71-11ef-8023-9fb6bfe1c003"),
        UUID.fromString("a3f1c723-9b71-11ef-8023-9fb6bfe1c004"),
        UUID.fromString("a3f1c724-9b71-11ef-8023-9fb6bfe1c005")
    );

    private final List<String> symbolList = List.of(
        "APPLE", "TATA", "HCL", "HDFC",
        "META", "GOOGLE", "CANARA",
        "AMD", "AMAZON", "NVIDIA"
    );

    private TradeEvent corruptTradeFields(TradeEvent trade) {

        if (random.nextInt(20000) != 0) return trade;

        int f = random.nextInt(6);
        switch (f) {
            case 0 -> trade.setPortfolioId(null);
            case 1 -> trade.setTradeId(null);
            case 2 -> trade.setSymbol(null);
            case 3 -> trade.setSide(null);
            case 4 -> trade.setPricePerStock(-1);
            case 5 -> trade.setQuantity(-5);
        }
        return trade;
    }

    public TradeEvent generateTrade() {

        speed();

        TradeEvent trade = new TradeEvent();
        trade.setPortfolioId(pList.get(random.nextInt(pList.size())));
        trade.setTradeId(UUID.randomUUID());
        trade.setSymbol(symbolList.get(random.nextInt(symbolList.size())));
        trade.setSide(random.nextBoolean() ? "BUY" : "SELL");
        trade.setPricePerStock(180 + random.nextDouble() * 20);
        trade.setQuantity(10 + random.nextInt(490));

        long hoursBack = 1 + random.nextInt(48);
        trade.setTimestamp(LocalDateTime.now().minusHours(hoursBack));

        return corruptTradeFields(trade);
    }

    private void speed() {
        long now = System.currentTimeMillis();

        if (now >= modeEndTime) {
            switch (currentMode) {
                case NORMAL -> currentMode = Mode.HYPE;
                case HYPE -> currentMode = Mode.PAUSE;
                case PAUSE -> currentMode = Mode.NORMAL;
            }
            modeEndTime = now + 3000 + random.nextInt(2000);
        }

        try {
            switch (currentMode) {
                case NORMAL -> Thread.sleep(100);
                case HYPE -> Thread.sleep(5);
                case PAUSE -> Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
