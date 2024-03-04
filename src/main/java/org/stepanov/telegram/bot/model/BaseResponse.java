package org.stepanov.telegram.bot.model;

public record BaseResponse(
        Boolean ok,
        Object result,
        Integer errorCode,
        String description) {
}
