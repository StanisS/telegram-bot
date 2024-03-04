package org.stepanov.telegram.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.stepanov.telegram.bot.model.Update;
import org.stepanov.telegram.bot.service.UpdateService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class TelegramController {

    private static final String TELEGRAM_BOT_SECRET_TOKEN_HEADER = "x-telegram-bot-api-secret-token";
    private final UpdateService updateService;

    @PostMapping(path = "/updates")
    public void receiveUpdate(@RequestBody Update update,
                              @RequestHeader Map<String, String> header) {
        updateService.processing(header.getOrDefault(TELEGRAM_BOT_SECRET_TOKEN_HEADER, "empty"), update);
    }
}
