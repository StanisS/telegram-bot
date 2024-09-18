package org.stepanov.telegram.bot.config;


import lombok.Data;

import java.util.List;

@Data
public class BotConfig {
        private String token;
        private String header;
        private List<Integer> part;
}
