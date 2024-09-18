package org.stepanov.telegram.bot.service;

import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.Update;

public interface BotService {
    BaseResponse processing(Update updateMessage);
    boolean isPartOf(Integer userId);
    String getHeader();

    void sendReplyMessage(String message, Update updateMessage);
}
