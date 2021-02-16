package com.andreyka.crypto;

public class SignatureValidationException extends RuntimeException {
    public SignatureValidationException(String message) {
        super(message);
    }

    public SignatureValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
