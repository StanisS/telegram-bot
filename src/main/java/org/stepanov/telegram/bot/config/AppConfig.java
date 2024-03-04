package org.stepanov.telegram.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.stepanov.telegram.bot.repository.EpRepository;
import org.stepanov.telegram.bot.repository.EpRepositoryFileImpl;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Value("${data.scv-path}")
    private String scvFilePath;

    @Bean
    @Primary
    public EpRepository createEpRepository() throws IOException {
        return new EpRepositoryFileImpl(scvFilePath);
    }
}
