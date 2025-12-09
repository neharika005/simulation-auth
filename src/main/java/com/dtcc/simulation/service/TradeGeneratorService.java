// package com.dtcc.simulation.service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Random;
// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled; 
// import org.springframework.stereotype.Service;

// import com.dtcc.simulation.entity.PortfolioId;
// import com.dtcc.simulation.entity.Symbol;
// import com.dtcc.simulation.model.TradeEvent;
// import com.dtcc.simulation.repository.PortfolioIdRepository;
// import com.dtcc.simulation.repository.SymbolRepository;

// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class TradeGeneratorService {

//     @Autowired
//     private PortfolioIdRepository portfolioRepo;
//     @Autowired
//     private SymbolRepository symbolRepo;

//     private final Random random = new Random(); 

//     private List<UUID> cachedPortfolios;
//     private List<String> cachedSymbols;

//     @PostConstruct
//     public void loadInitialDataFromDB() {
//         this.cachedSymbols = symbolRepo.findAll()
//                 .stream()
//                 .map(Symbol::getSymbol)
//                 .toList();

//         updatePortfolioIdCache(); 
        
//         System.out.println("Initial Cache Load Complete. Symbols: " + cachedSymbols.size());
//     }

//     @Scheduled(fixedRate = 1000)
//     public void updatePortfolioIdCache() {
//         System.out.println("--- Starting Portfolio ID cache update: " + LocalDateTime.now() + " ---");

//         List<UUID> newPortfolios = portfolioRepo.findAll()
//                 .stream()
//                 .map(PortfolioId::getPortfolio_id)
//                 .toList();

//         this.cachedPortfolios = newPortfolios; 
        
//         System.out.println("Portfolio ID cache updated. New count: " + newPortfolios.size());
//     }


//     public TradeEvent generateTrade() {

//         if (cachedPortfolios == null || cachedPortfolios.isEmpty() || cachedSymbols == null || cachedSymbols.isEmpty()) {
//              throw new IllegalStateException("Trade generator cache is empty. Cannot generate trade.");
//         }
        
//         TradeEvent t = new TradeEvent();

//         t.setPortfolioId(
//                 cachedPortfolios.get(random.nextInt(cachedPortfolios.size()))
//         );

//         boolean missingFields = random.nextDouble() < 0.20;
//         boolean invalidTrade = random.nextDouble() < 0.10;

//         if (missingFields) {

//             return t; 
//         }

//         if (invalidTrade) {
//             t.setTradeId(UUID.randomUUID());
//             t.setPricePerStock(-1 * (10 + random.nextDouble(50)));
//             t.setQuantity(-1L * (1 + random.nextInt(20)));
//             t.setTimestamp(
//                 LocalDateTime.now()
//                     .plusHours(1 + random.nextInt(24))
//                     .withNano(random.nextInt(1_000_000_000))
//             );

//             t.setSymbol(
//                     cachedSymbols.get(random.nextInt(cachedSymbols.size()))
//             );
//             t.setSide(random.nextBoolean() ? "BUY" : "SELL");
//             return t;
//         }

//         t.setTradeId(UUID.randomUUID());

//         t.setSymbol(
//                 cachedSymbols.get(random.nextInt(cachedSymbols.size()))
//         );

//         t.setSide(random.nextBoolean() ? "BUY" : "SELL");
//         t.setPricePerStock(100 + random.nextDouble(101));
//         t.setQuantity(1 + random.nextLong(100));
//         t.setTimestamp(
//             LocalDateTime.now()
//                 .minusHours(random.nextInt(24) + 1)
//                 .withNano(random.nextInt(1_000_000_000))
//         );

//         return t;
//     }
// }

package com.dtcc.simulation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled; 
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

    @Autowired
    private PortfolioIdRepository portfolioRepo;
    @Autowired
    private SymbolRepository symbolRepo;

    private final Random random = new Random(); 

    private List<UUID> cachedPortfolios;
    private List<String> cachedSymbols;

    @PostConstruct
    public void loadInitialDataFromDB() {
        this.cachedSymbols = symbolRepo.findAll()
                .stream()
                .map(Symbol::getSymbol)
                .toList();

        updatePortfolioIdCache(); 
    }

    @Scheduled(fixedRate = 30000)//5 mins
    public void updatePortfolioIdCache() {
        List<UUID> newPortfolios = portfolioRepo.findAll()
                .stream()
                .map(PortfolioId::getPortfolio_id)
                .toList();

        this.cachedPortfolios = newPortfolios; 
    }

    public TradeEvent generateTrade() {

        if (cachedPortfolios == null || cachedPortfolios.isEmpty() || cachedSymbols == null || cachedSymbols.isEmpty()) {
             throw new IllegalStateException("Trade generator cache is empty. Cannot generate trade.");
        }
        
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