package com.oleg.customer.costs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MonobankConfig {

    @Bean
    public WebClient monobankWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.monobank.ua")
            .build();
    }
}
