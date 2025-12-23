package com.dtcc.simulation.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dtcc.simulation.auth.OAuthTokenService;

@Service
public class GatewayClient {

    private final OAuthTokenService tokenService;
    private final WebClient webClient;

    public GatewayClient(OAuthTokenService tokenService,
                         WebClient.Builder builder) {
        this.tokenService = tokenService;
        this.webClient = builder.build();
    }

    public String callGatewayApi() {

        String token = tokenService.getAccessToken();

        return webClient.get()
                .uri("http://localhost:8081/api/test")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
