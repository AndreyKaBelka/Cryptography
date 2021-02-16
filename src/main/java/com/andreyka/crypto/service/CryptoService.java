package com.andreyka.crypto.service;

import com.andreyka.crypto.CryptoOperationException;
import com.andreyka.crypto.SignatureValidationException;
import com.andreyka.crypto.data.ECPoint;

import java.math.BigInteger;

public interface CryptoService {
    Pair encrypt(BigInteger commonKey, BigInteger yourPrivateKey, String text) throws CryptoOperationException;

    String decrypt(BigInteger commonKey, ECPoint otherPublicKey, Pair pair) throws CryptoOperationException, SignatureValidationException;

    class Pair {
        String text;
        ECPoint signature;

        public Pair(String text, ECPoint signature) {
            this.text = text;
            this.signature = signature;
        }
    }
}
