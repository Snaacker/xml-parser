package com.snaacker.sample.exception;

public class XMLParserNotFoundException extends RuntimeException {
    public XMLParserNotFoundException(Throwable cause) {
        super(cause);
    }

    public XMLParserNotFoundException(String message) {
        super(message);
    }

    public XMLParserNotFoundException(String message, Throwable cause) {
        super(message);
    }

    public XMLParserNotFoundException() {
        super();
    }
}
