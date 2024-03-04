package org.stepanov.telegram.bot.repository.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EpEntity(Long id, Scope scope, LocalDate date, Integer number) {
    public String toCSVString() {
        return (id == null ? "" : id) + "," +
                (scope == null ? "" : scope.name()) + "," +
                (date == null ? "" : date.toString()) + "," +
                (number == null ? "" : number.toString());
    }
}
