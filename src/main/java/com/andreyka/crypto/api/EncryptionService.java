package com.andreyka.crypto.api;

import com.andreyka.crypto.ReflectionUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public interface EncryptionService {
    static EncryptionService getService(String alg) throws NoSuchAlgorithmException {
        return ReflectionUtils.getInstance(EncryptionService.class, alg);
    }

    byte[] encrypt(String text, BigInteger commonKey);

    byte[] decrypt(byte[] text, BigInteger commonKey);
}
