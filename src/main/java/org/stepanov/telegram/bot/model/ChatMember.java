package org.stepanov.telegram.bot.model;

public record ChatMember(String status, User user, Boolean isAnonymous) {
}
