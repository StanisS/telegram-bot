package org.stepanov.telegram.bot.model;

import java.util.List;

public record UsersShared(Integer requestId, List<Integer> userIds) {
}

