package com.dtcc.simulation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dtcc.simulation.entity.PortfolioId;
import com.dtcc.simulation.entity.Symbol;
import com.dtcc.simulation.model.TradeEvent;
import com.dtcc.simulation.repository.PortfolioIdRepository;
import com.dtcc.simulation.repository.SymbolRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeGeneratorService {

    private PortfolioIdRepository portfolioRepo;
    private SymbolRepository symbolRepo;

    private Random random = new Random();

    private List<UUID> cachedPortfolios;
    private List<String> cachedSymbols;

    @PostConstruct
    public void loadDataFromDB() {
        cachedPortfolios = portfolioRepo.findAll()
                .stream()
                .map(PortfolioId::getPortfolio_id)
                .toList();

        cachedSymbols = symbolRepo.findAll()
                .stream()
                .map(Symbol::getSymbol)
                .toList();
    }

    public TradeEvent generateTrade() {

        TradeEvent t = new TradeEvent();

        t.setPortfolioId(
                cachedPortfolios.get(random.nextInt(cachedPortfolios.size()))
        );

        boolean missingFields = random.nextDouble() < 0.20;

        boolean invalidTrade = random.nextDouble() < 0.10;

        if (missingFields) {
            return t;
        }

        if (invalidTrade) {
            t.setTradeId(UUID.randomUUID());
            t.setPricePerStock(-1 * (10 + random.nextDouble(50)));
            t.setQuantity(-1L * (1 + random.nextInt(20)));
            t.setTimestamp(
                LocalDateTime.now()
                    .plusHours(1 + random.nextInt(24))
                    .withNano(random.nextInt(1_000_000_000))
            );

            t.setSymbol(
                    cachedSymbols.get(random.nextInt(cachedSymbols.size()))
            );
            t.setSide(random.nextBoolean() ? "BUY" : "SELL");
            return t;
        }

        t.setTradeId(UUID.randomUUID());

        t.setSymbol(
                cachedSymbols.get(random.nextInt(cachedSymbols.size()))
        );

        t.setSide(random.nextBoolean() ? "BUY" : "SELL");
        t.setPricePerStock(100 + random.nextDouble(101));
        t.setQuantity(1 + random.nextLong(100));
        t.setTimestamp(
            LocalDateTime.now()
                .minusHours(random.nextInt(24) + 1)
                .withNano(random.nextInt(1_000_000_000))
);

        return t;
    }
}
