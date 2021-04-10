package com.andreyka.crypto;

class CryptoOperationException extends RuntimeException {
    CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
