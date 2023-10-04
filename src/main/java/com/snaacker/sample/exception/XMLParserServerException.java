package com.snaacker.sample.exception;

public class XMLParserServerException extends RuntimeException {
    public XMLParserServerException(Throwable cause) {
        super(cause);
    }

    public XMLParserServerException(String message) {
        super(message);
    }

    public XMLParserServerException(String message, Throwable cause) {
        super(message);
    }

    public XMLParserServerException() {
        super();
    }
}
