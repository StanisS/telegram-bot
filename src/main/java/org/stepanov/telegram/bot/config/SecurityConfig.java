package org.stepanov.telegram.bot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRegistry -> {
            authRegistry.requestMatchers("/updates*", "/statistics*")
                    .permitAll();
            authRegistry.requestMatchers("/eps/**").access((authentication, object) -> {
                log.info(authentication.get());
                log.info(object.getRequest());
                return new AuthorizationDecision(true);
            });
        });

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
