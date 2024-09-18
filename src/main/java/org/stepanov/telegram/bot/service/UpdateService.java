package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.Update;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UpdateService {

    private final MessageService messageService;

    private final PapashkiService papashkiService;
    private final FamilyService familyService;

    public void processing(String botHeader, Update updateMessage) {
        if (papashkiService.getHeader().equals(botHeader)) {
            log.info("bot papashki request");
            checkSender(papashkiService, updateMessage);
            BaseResponse response = papashkiService.processing(updateMessage);
            log.info("bot papashki response [{}]", response);
        } else if (familyService.getHeader().equals(botHeader)) {
            log.info("bot family request");
            checkSender(familyService, updateMessage);
            BaseResponse response = familyService.processing(updateMessage);
            log.info("bot family response [{}]", response);
        } else if ("empty".equals(botHeader)) {
            log.warn("bot empty request");
        } else {
            log.warn("bot unknown request [{}]", botHeader);
        }
    }

    private void checkSender(BotService botService, Update updateMessage) {
        Integer userId = Optional.ofNullable(updateMessage.message().from().id()).orElseThrow();

        if (!botService.isPartOf(userId)) {
            String warningMessage = String.format("Request from an unknown user to %s%n%s%n%s",
                    botService.getClass(),
                    updateMessage.message().text(),
                    updateMessage.message().from());
            log.warn(warningMessage);
            messageService.notifyAdmins(warningMessage, updateMessage);

            String message = "I don't trust you " + updateMessage.message().from().username() + ".";

            botService.sendReplyMessage(message, updateMessage);
        }
    }


}
