package org.stepanov.telegram.bot.model;

import lombok.Builder;

@Builder
public record SendMessage (
        Integer chatId,
//        message_thread_id	Integer
        String text,
        String parseMode,
//        entities	Array of MessageEntity
//        link_preview_options	LinkPreviewOptions
//        disable_notification	Boolean
        Boolean protectContent,
        ReplyParameters replyParameters
//        reply_markup
) {
}
