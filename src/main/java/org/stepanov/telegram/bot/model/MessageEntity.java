package org.stepanov.telegram.bot.model;

public record MessageEntity(
        String type,
        Integer offset,
        Integer length,
        String url,
        User user,
        String language,
        String customEmojiId) {
}
