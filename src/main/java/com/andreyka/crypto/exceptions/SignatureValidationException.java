package com.andreyka.crypto.exceptions;

public class SignatureValidationException extends RuntimeException {
    public SignatureValidationException(String message) {
        super(message);
    }
}
