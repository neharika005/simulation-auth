package com.dtcc.simulation.client;

import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatewayClient {

    private final WebClient webClient;

    public String callGatewayApi() {

        return webClient
                .get()
                // .uri("http://api-gateway:4000/api/test")
                .uri("http://auth:8080/actuator/health")
                .attributes(
                    ServletOAuth2AuthorizedClientExchangeFilterFunction
                        .clientRegistrationId("gateway-client")
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
