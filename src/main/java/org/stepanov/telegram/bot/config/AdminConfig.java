package org.stepanov.telegram.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.admin")
public record AdminConfig(Integer userId, Integer chatId, String token) {
}
