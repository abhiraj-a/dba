package com.dba;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClerkClient {

    private final WebClient  webClient;

    public ClerkClient(@Value("${clerk.api-secret}") String clerkSecretKey){
        this.webClient = WebClient.builder()
                .baseUrl("https://api.clerk.com/v1")
                .defaultHeader("Authorization" , "Bearer "+clerkSecretKey)
                .build();
    }

    public void deleteUser(String principal){
        webClient.delete()
                .uri("/users/{id}" , principal)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
