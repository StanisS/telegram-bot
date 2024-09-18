package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.stepanov.telegram.bot.config.AdminConfig;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.ReplyParameters;
import org.stepanov.telegram.bot.model.SendMessage;
import org.stepanov.telegram.bot.model.Update;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageService {

    @Qualifier("telegramClient")
    private final WebClient telegramClient;
    private final AdminConfig adminConfig;

    public BaseResponse sendMessage(SendMessage message, String token) {
        return telegramClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/bot{token}/sendMessage")
                        .build(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(message))
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();
    }

    public void notifyAdmins(String warningMessage, Update updateMessage) {
        SendMessage notifyMessage = SendMessage.builder()
                .chatId(adminConfig.chatId())
                .text(warningMessage)
                .build();
        sendMessage(notifyMessage, adminConfig.token());
    }

    public SendMessage getReplyMessage(Update updateMessage, String message, String parseMode) {
        return SendMessage.builder()
                .chatId(updateMessage.message().chat().id())
                .replyParameters(ReplyParameters.builder()
                        .messageId(updateMessage.message().messageId())
                        .chatId(updateMessage.message().chat().id())
                        .build())
                .text(message)
                .parseMode(parseMode)
                .build();
    }
}
