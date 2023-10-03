package com.snaacker.sample.exception;

public class XMLParserException extends RuntimeException {
    public XMLParserException(Throwable cause) {
        super(cause);
    }

    public XMLParserException(String message) {
        super(message);
    }

    public XMLParserException(String message, Throwable cause) {
        super(message);
    }

    public XMLParserException() {
        super();
    }
}
