package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.config.PapashkiConfig;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.ReplyParameters;
import org.stepanov.telegram.bot.model.SendMessage;
import org.stepanov.telegram.bot.model.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PapashkiService {

    private final PapashkiConfig papashkiConfig;
    private final MessageService messageService;
    private final EpService epService;

    public BaseResponse processing(Update updateMessage) {
        Integer userId = Optional.ofNullable(updateMessage.message().from().id()).orElseThrow();
        String message;
        if (!isTrusted(userId)) {
            String warningMessage = String.format("Request from an unknown user to Papashki%n%s%n%s",
                    updateMessage.message().text(),
                    updateMessage.message().from());
            log.warn(warningMessage);
            messageService.notifyAdmins(warningMessage, updateMessage);

            message = "I don't trust you " + updateMessage.message().from().username() + ".";
        } else {
            String text = updateMessage.message().text();

            if (text.startsWith("/set_ep_bel")) {
                String[] data = text.split("-");
                if (data.length < 3) {
                    message = "Please add details. Example: /set_ep_bel - 7 Feb 2014 - 234";
                } else {
                    Integer amount = Integer.valueOf(data[2].trim());
                    LocalDate date = LocalDate.parse(data[1].trim(),
                            DateTimeFormatter.ofPattern("d MMM yyyy"));

                    epService.setBel(date, amount);
                    message = String.format("I added data for Bel [%1$te %1$tb %1$tY - %2$d]", date, amount);
                }
            } else if (text.startsWith("/set_ep_global")) {
                String[] data = text.split("-");
                if (data.length < 3) {
                    message = "Please add details. Example: /set_ep_global - 7 Feb 2014 - 234";
                } else {
                    Integer amount = Integer.valueOf(data[2].trim());
                    LocalDate date = LocalDate.parse(data[1].trim(),
                            DateTimeFormatter.ofPattern("d MMM yyyy"));

                    epService.setGlobal(date, amount);
                    message = String.format("I added data for Global [%1$te %1$tb %1$tY - %2$d]", date, amount);
                }
            } else if (text.startsWith("/get_ep_bel")) {
                message = createMessage(epService.getBel());
            } else if (text.startsWith("/get_ep_global")) {
                message = createMessage(epService.getGlobal());
            } else {
                message = "I'm glad you asked me about [" + text + "], but I know nothing about it.";
            }
        }

        SendMessage replyMessage = getReplyMessage(updateMessage, message, "HTML");

        return messageService.sendMessage(replyMessage, papashkiConfig.token());
    }

    private static String createMessage(Map<LocalDate, Integer> map) {
        String message;
        message = """
                <pre>
                +-------------+--------+-------+--------+
                |    Date     | Number |  Days |  Diff  |
                +-------------+--------+-------+--------+""";
        StringBuilder tmp = new StringBuilder();
        Map.Entry<LocalDate, Integer> pre = null;

        for (Map.Entry<LocalDate, Integer> entry : map.entrySet()) {
            LocalDate localDate = entry.getKey();
            Integer integer = entry.getValue();
            long days = 0;
            int diff = 0;
            if (pre != null) {
                days = ChronoUnit.DAYS.between(pre.getKey(), localDate);
                diff = integer - pre.getValue();
            }
            tmp.append(String.format("%n| %1$td %1$tb %1$tY | %2$6d | %3$5d | %4$6d |%n", localDate, integer, days, diff));
            tmp.append("+-------------+--------+-------+--------+");
            pre = entry;
        }
        message += tmp + "\n</pre>";
        return message;
    }

    private static SendMessage getReplyMessage(Update updateMessage, String message, String parseMode) {
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

    private boolean isTrusted(Integer userId) {
        return papashkiConfig.trusted().contains(userId);
    }
}
