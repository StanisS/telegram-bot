package org.stepanov.telegram.bot.model;

public record ChatMemberUpdated(Chat chat, User from, Integer date, ChatMember oldChatMember,
                                ChatMember newChatMember, ChatInviteLink inviteLink,
                                Boolean viaChatFolderInviteLink) {
}
