package com.snaacker.sample.exception;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(XMLParserException.class)
    protected ResponseEntity<Object> handleBadRequest(RuntimeException exception, WebRequest request) {
        String bodyOfResponse = "Bad request";
        return handleExceptionInternal(
                exception, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({XMLParserServerException.class, IOException.class})
    protected ResponseEntity<Object> handleCrash(RuntimeException exception, WebRequest request) {
        String bodyOfResponse = "Internal Server Error";
        return handleExceptionInternal(
            exception, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
