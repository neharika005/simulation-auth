package com.dtcc.simulation.controller;

import com.dtcc.simulation.model.PortfolioCreateRequest;
import com.dtcc.simulation.service.PortfolioManagerService;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
public class PortfolioSimulationController {

    private final PortfolioManagerService portfolioManagerService;

    public PortfolioSimulationController(PortfolioManagerService portfolioManagerService) {
        this.portfolioManagerService = portfolioManagerService;
    }

    @PostMapping("/create-portfolio")
    public UUID createPortfolio(@RequestBody PortfolioCreateRequest request) {
        return portfolioManagerService.createAndStorePortfolio(request);
    }
}
