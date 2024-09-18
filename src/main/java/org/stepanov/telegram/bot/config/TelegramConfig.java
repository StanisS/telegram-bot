package org.stepanov.telegram.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TelegramConfig {

    @Value("${telegram.host}")
    private String host;

    @Bean(name = "telegramClient")
    public WebClient telegramClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(host)
                .build();
    }

    @Bean(name = "familyConfig")
    @ConfigurationProperties(prefix = "telegram.family")
    public BotConfig getFamilyBotConfiguration() {
        return new BotConfig();
    }

    @Bean(name = "papashkiConfig")
    @ConfigurationProperties(prefix = "telegram.papashki")
    public BotConfig getPapashkiBotConfiguration() {
        return new BotConfig();
    }
}
