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
                Object[] data = parseMessage(text);
                if (data == null) {
                    message = "Please add details. \"command\" \"amount\" \"date YYYY-MM-DD\" Example: /set_ep_bel 234 2014-02-04";
                } else {
                    LocalDate date = (LocalDate) data[1];
                    Integer amount = (Integer) data[0];
                    epService.setBel(date, amount);
                    message = String.format("I added data for Bel [%1$te %1$tb %1$tY - %2$d]", date, amount);
                }
            } else if (text.startsWith("/set_ep_global")) {
                Object[] data = parseMessage(text);
                if (data == null) {
                    message = "Please add details. \"command\" \"amount\" \"date YYYY-MM-DD\" Example: /set_ep_global 234 2014-02-04";
                } else {
                    LocalDate date = (LocalDate) data[1];
                    Integer amount = (Integer) data[0];
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

    private Object[] parseMessage(String text) {
        String[] data = text.split(" ");
        if (data.length < 2 || data.length > 3) {
            return null;
        }
        Integer amount = Integer.valueOf(data[1].trim());
        LocalDate date;
        if (data.length == 2) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(data[2].trim());
        }
        return new Object[]{amount, date};
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
