package com.andreyka.crypto;

public class SignatureValidationException extends RuntimeException {
    public SignatureValidationException(String message) {
        super(message);
    }
}
