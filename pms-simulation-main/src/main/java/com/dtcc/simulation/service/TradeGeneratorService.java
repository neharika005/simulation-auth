// package com.dtcc.simulation.service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Random;
// import java.util.UUID;

// import org.springframework.stereotype.Service;

// import com.dtcc.simulation.model.TradeEvent;

// @Service
// public class TradeGeneratorService {

//     private  Random random = new Random();

//     private  List<UUID> portfolios = List.of(
//             UUID.fromString("3f2c1d4e-8b57-4a92-9c65-b5e6f4e19b73"),
//             UUID.fromString("a8d4c0fa-7c1b-4e5d-9a89-2d635f0e2a14"),
//             UUID.fromString("d91f0b62-4c2f-4b91-8f2f-1b6c4ad7e543"),
//             UUID.fromString("59c8a6d1-3f8b-4a67-bab6-89d0e72cce10"),
//             UUID.fromString("e2fa4c39-2a65-41c8-9f91-3c57f1d900ba")
//     );

//     private  List<String> symbols = List.of(
//             "AAPL", "MSFT", "GOOGL", "AMZN", "META",
//             "NVDA", "TSLA", "NFLX", "AMD", "INTC",
//             "IBM", "ORCL", "BAC", "JPM", "WMT"
//     );

//     public TradeEvent generateTrade() {

//         TradeEvent t = new TradeEvent();

//         t.setPortfolioId(portfolios.get(random.nextInt(portfolios.size())));

//         boolean missingFields = random.nextDouble() < 0.20;

        
//         boolean invalidTrade = random.nextDouble() < 0.10;

//         if (missingFields) {
            
//             return t;
//         }
//         if (invalidTrade) {
            
//             t.setTradeId(UUID.randomUUID());
            
//             t.setPricePerStock(-1 * (10 + random.nextDouble(50)));
//             t.setQuantity(-1L * (1 + random.nextInt(20)));
            
//             t.setTimestamp(LocalDateTime.now().plusHours(1 + random.nextInt(24)));
            
//             t.setSymbol(symbols.get(random.nextInt(symbols.size())));
//             t.setSide(random.nextBoolean() ? "BUY" : "SELL");

//             return t;
//         }

//         t.setTradeId(UUID.randomUUID());
//         t.setSymbol(symbols.get(random.nextInt(symbols.size())));
//         t.setSide(random.nextBoolean() ? "BUY" : "SELL");

//         t.setPricePerStock(100 + random.nextDouble(101));
        
//         t.setQuantity(1 + random.nextLong(100));
        
//         t.setTimestamp(LocalDateTime.now().minusHours(random.nextInt(24) + 1));

//         return t;
//     }
// }




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

    private final PortfolioIdRepository portfolioRepo;
    private final SymbolRepository symbolRepo;

    private final Random random = new Random();

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
