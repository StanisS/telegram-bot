package org.stepanov.telegram.bot.model;

public record User(
        Integer id,
        Boolean isBot,
        String firstName,
        String lastName,
        String username,
        String languageCode,
        Boolean isPremium,
        Boolean addedToAttachmentMenu,
        Boolean canJoinGroups,
        Boolean canReadAllGroupMessages,
        Boolean supportsInlineQueries) {
}
