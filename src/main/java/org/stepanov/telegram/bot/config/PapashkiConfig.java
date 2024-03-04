package org.stepanov.telegram.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "telegram.papashki")
public record PapashkiConfig(
        String token,
        List<Integer> trusted) {
}
