package com.andreyka.crypto;

public class CryptoOperationException extends RuntimeException {
    public CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
