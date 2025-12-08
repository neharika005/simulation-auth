package com.dtcc.simulation.service;

import com.dtcc.simulation.entity.PortfolioId;
import com.dtcc.simulation.model.PortfolioCreateRequest;
import com.dtcc.simulation.repository.PortfolioIdRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PortfolioManagerService {

    private final PortfolioClientService portfolioClientService;
    private final PortfolioIdRepository portfolioIdRepository;

    public PortfolioManagerService(PortfolioClientService portfolioClientService,
                                   PortfolioIdRepository portfolioIdRepository) {
        this.portfolioClientService = portfolioClientService;
        this.portfolioIdRepository = portfolioIdRepository;
    }

    public UUID createAndStorePortfolio(PortfolioCreateRequest request) {

        // Step 1: Call Portfolio Service
        UUID newPortfolioId = portfolioClientService.createPortfolio(request);

        // Step 2: Save in simulation DB
        PortfolioId portfolioId = new PortfolioId(newPortfolioId);
        portfolioIdRepository.save(portfolioId);

        return newPortfolioId;
    }
}
