package com.andreyka.crypto.exceptions;

public class CryptoOperationException extends RuntimeException {
    public CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoOperationException(String message) {
        super(message);
    }
}
