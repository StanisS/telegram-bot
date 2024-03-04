package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.Update;

@Service
@RequiredArgsConstructor
@Log4j2
public class UpdateService {

    private final PapashkiService papashkiService;

    public void processing(String botHeader, Update updateMessage) {
        switch (botHeader) {
            case "papashki-06-02-24" -> {
                log.info("bot papashki request");
                BaseResponse response = papashkiService.processing(updateMessage);
                log.info("bot papashki response [{}]", response);
            }
            case "empty" -> log.warn("bot empty request");
            default -> log.warn("bot unknown request [{}]", botHeader);
        }
    }
}
