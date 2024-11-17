package org.example;

public class GameInitializationException extends RuntimeException {
    public GameInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}