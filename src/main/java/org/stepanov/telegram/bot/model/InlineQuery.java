package org.stepanov.telegram.bot.model;

public record InlineQuery (
        String id,
        User from,
        String query,
        String offset,
        String chatType,
        Location location) { }
