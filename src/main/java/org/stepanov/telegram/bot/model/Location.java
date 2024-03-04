package org.stepanov.telegram.bot.model;

public record Location(Float longitude, Float latitude, Float horizontalAccuracy, Integer livePeriod, Integer heading,
                       Integer proximityAlertRadius) {
}
