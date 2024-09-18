package org.stepanov.telegram.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.stepanov.telegram.bot.service.EpService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "/eps")
@RequiredArgsConstructor
@Log4j2
public class EpController {

    private final EpService epService;

    @DeleteMapping(path = "/{scope}/{date}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEps(@PathVariable String scope, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        epService.deleteRecord(scope,localDate);
    }
}
