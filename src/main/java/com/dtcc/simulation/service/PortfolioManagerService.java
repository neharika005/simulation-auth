package com.dtcc.simulation.service;

import com.dtcc.simulation.entity.PortfolioId;
import com.dtcc.simulation.model.PortfolioCreateRequest;
import com.dtcc.simulation.repository.PortfolioIdRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PortfolioManagerService {

    private PortfolioClientService portfolioClientService;
    private PortfolioIdRepository portfolioIdRepository;

    public PortfolioManagerService(PortfolioClientService portfolioClientService,
                                   PortfolioIdRepository portfolioIdRepository) {
        this.portfolioClientService = portfolioClientService;
        this.portfolioIdRepository = portfolioIdRepository;
    }

    public UUID createAndStorePortfolio(PortfolioCreateRequest request) {

        UUID newPortfolioId = portfolioClientService.createPortfolio(request);

        PortfolioId portfolioId = new PortfolioId(newPortfolioId);
        portfolioIdRepository.save(portfolioId);

        return newPortfolioId;
    }
}
