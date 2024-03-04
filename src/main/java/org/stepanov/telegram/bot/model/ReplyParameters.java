package org.stepanov.telegram.bot.model;

import lombok.Builder;

@Builder
public record ReplyParameters(
        Integer messageId,
        Integer chatId,
        Boolean allowSendingWithoutReply,
        String quote,
        String quoteParseMode,
//        quote_entities	Array of MessageEntity
        Integer quotePosition
) {
}
