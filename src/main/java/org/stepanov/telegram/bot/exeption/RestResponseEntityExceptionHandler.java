package org.stepanov.telegram.bot.exeption;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Log4j2
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class, Error.class})
    @ResponseStatus(HttpStatus.OK)
    protected void handleConflict(RuntimeException ex, WebRequest request) {

        log.error("Path: " + request.getContextPath(), ex);
    }
}
