package com.andreyka.crypto;

class SignatureValidationException extends RuntimeException {
    SignatureValidationException(String message) {
        super(message);
    }

    SignatureValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
