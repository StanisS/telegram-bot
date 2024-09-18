package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.config.BotConfig;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.SendMessage;
import org.stepanov.telegram.bot.model.Update;

@Service
@RequiredArgsConstructor
@Log4j2
public class FamilyService implements BotService {

    @Qualifier("familyConfig")
    private final BotConfig familyConfig;
    private final MessageService messageService;
    private final AutomationService automationService;

    @Override
    public BaseResponse processing(Update updateMessage) {
        String message;
        String text = updateMessage.message().text();

        if (text.startsWith("/get_home_temperature")) {
            message = automationService.getTemperature();
        } else {
            message = "Sorry, but I'm still learning and don't know what to do with [" + text + "]";
        }
        log.info("family reply message: " + message);
        SendMessage replyMessage = messageService.getReplyMessage(updateMessage, message, "HTML");

        return messageService.sendMessage(replyMessage, familyConfig.getToken());
    }

    @Override
    public boolean isPartOf(Integer userId) {
        return familyConfig.getPart().contains(userId);
    }

    @Override
    public String getHeader() {
        return familyConfig.getHeader();
    }

    @Override
    public void sendReplyMessage(String message, Update updateMessage) {
        SendMessage replyMessage = messageService.getReplyMessage(updateMessage, message, "HTML");
        messageService.sendMessage(replyMessage, familyConfig.getToken());
    }
}
