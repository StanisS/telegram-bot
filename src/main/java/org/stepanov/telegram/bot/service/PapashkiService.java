package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.config.BotConfig;
import org.stepanov.telegram.bot.model.BaseResponse;
import org.stepanov.telegram.bot.model.SendMessage;
import org.stepanov.telegram.bot.model.Update;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class PapashkiService implements BotService {
    public static final String GLOBAL_COUNTRY = "global";

    @Qualifier("papashkiConfig")
    private final BotConfig papashkiConfig;
    private final MessageService messageService;
    private final EpService epService;

    @Override
    public BaseResponse processing(Update updateMessage) {
        String message;
        String text = updateMessage.message().text();
        if (text.startsWith("/set_ep")) {
            Object[] data = parseSetMessage(text);
            if (data == null) {
                message = """
                        Please add details. /set_ep \"country\"(default global) \"amount\" \"date YYYY-MM-DD\"
                        "Example:
                        /set_ep 234
                        /set_ep 234 2014-02-04
                        /set_ep by 234
                        /set_ep by 234 2014-02-04""";
            } else {
                String country = (String) data[0];
                Integer amount = (Integer) data[1];
                LocalDate date = (LocalDate) data[2];
                epService.setAmount(country, amount, date, updateMessage.message().from().id());
                message = String.format("I added data for %1$s [%2$te %2$tb %2$tY - %3$d]", country, date, amount);
            }
        } else if (text.startsWith("/get_ep")) {
            String country = parseGetMessage(text);
            if (country == null) {
                message = """
                        Please check your command. /get_ep \"country\"(default global)
                        "Example:
                        /get_ep
                        /get_ep by""";
            } else {
                message = createMessage(epService.getAmount(country));
            }
        } else {
            message = "I'm glad you asked me about [" + text + "], but I know nothing about it.";
        }

        SendMessage replyMessage = messageService.getReplyMessage(updateMessage, message, "HTML");

        return messageService.sendMessage(replyMessage, papashkiConfig.getToken());
    }

    @Override
    public boolean isPartOf(Integer userId) {
        return papashkiConfig.getPart().contains(userId);
    }

    @Override
    public String getHeader() {
        return papashkiConfig.getHeader();
    }

    @Override
    public void sendReplyMessage(String message, Update updateMessage) {
        SendMessage replyMessage = messageService.getReplyMessage(updateMessage, message, "HTML");
        messageService.sendMessage(replyMessage, papashkiConfig.getToken());
    }

    private String parseGetMessage(String text) {
        String[] data = text.split(" ");
        if (data.length > 2) {
            return null;
        } else if (data.length == 2) {
            return data[1];
        } else {
            return GLOBAL_COUNTRY;
        }
    }

    private Object[] parseSetMessage(String text) {
        String[] data = text.split(" ");
        if (data.length < 2 || data.length > 4) {
            return null;
        }
        String country;
        Integer amount;
        LocalDate date;
        if (data.length == 2) {
            country = GLOBAL_COUNTRY;
            amount = Integer.valueOf(data[1].trim());
            date = LocalDate.now();
        } else if (data.length == 3) {
            if (data[1].matches("\\w{2}")) {
                country = data[1];
                amount = Integer.valueOf(data[2].trim());
                date = LocalDate.now();
            } else {
                country = GLOBAL_COUNTRY;
                amount = Integer.valueOf(data[1].trim());
                date = LocalDate.parse(data[2].trim());
            }
        } else {
            country = data[1];
            amount = Integer.valueOf(data[2].trim());
            date = LocalDate.parse(data[3].trim());
        }
        return new Object[]{country, amount, date};
    }

    private static String createMessage(Map<LocalDate, Integer> map) {
        String message;
        message = """
                <pre>
                +--------------+--------+-------+--------+
                |    Date      | Number |  Days |  Diff  |
                +--------------+--------+-------+--------+""";
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
            tmp.append(String.format("%n| %1$td %1$-4tb %1$tY | %2$6d | %3$5d | %4$6d |%n", localDate, integer, days, diff));
            tmp.append("+--------------+--------+-------+--------+");
            pre = entry;
        }
        message += tmp + "\n</pre>";
        return message;
    }

}
