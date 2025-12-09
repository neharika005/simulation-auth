package com.dtcc.simulation.service;

import com.dtcc.simulation.model.PortfolioCreateRequest;
import com.dtcc.simulation.model.PortfolioCreateResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class PortfolioClientService {

    private RestTemplate restTemplate;

    @Value("${portfolio.service.url}")
    private String portfolioServiceUrl;

    public UUID createPortfolio(PortfolioCreateRequest request) {

        String url = portfolioServiceUrl + "/api/portfolio/create";

        ResponseEntity<PortfolioCreateResponse> response =
                restTemplate.postForEntity(url, request, PortfolioCreateResponse.class);

        return response.getBody().getPortfolioId();
    }
}
