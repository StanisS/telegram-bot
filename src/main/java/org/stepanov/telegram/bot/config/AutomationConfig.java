package org.stepanov.telegram.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class AutomationConfig {

    @Value("${automation.host}")
    private String host;

    @Bean(name = "automationClient")
    public WebClient client(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(host)
                .build();
    }
}
