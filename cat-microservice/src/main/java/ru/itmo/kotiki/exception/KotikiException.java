package ru.itmo.kotiki.exception;

public class KotikiException extends RuntimeException {
    public KotikiException() {
        super();
    }
    public KotikiException(String message) {
        super(message);
    }
}