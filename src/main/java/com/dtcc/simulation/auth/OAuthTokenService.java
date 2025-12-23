package com.dtcc.simulation.auth;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OAuthTokenService {

    private final WebClient webClient;

    private String cachedToken;
    private Instant expiryTime;

    @Value("${auth.server.token-url}")
    private String tokenUrl;

    @Value("${auth.client.id}")
    private String clientId;

    @Value("${auth.client.secret}")
    private String clientSecret;

    public OAuthTokenService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public synchronized String getAccessToken() {

        if (cachedToken != null && expiryTime != null && Instant.now().isBefore(expiryTime)) {
            return cachedToken;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        TokenResponse response = webClient.post()
                .uri(tokenUrl)
                .headers(h -> h.setBasicAuth(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if (response == null || response.getAccessToken() == null) {
            throw new IllegalStateException("Auth server did not return token");
        }

        this.cachedToken = response.getAccessToken();
        this.expiryTime = Instant.now().plusSeconds(250);

        return cachedToken;
    }
}
