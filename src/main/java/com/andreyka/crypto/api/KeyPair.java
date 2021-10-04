package com.andreyka.crypto.api;

import com.andreyka.crypto.ReflectionUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class KeyPair {
    private ECPoint publicKey;
    private BigInteger privateKey;

    public KeyPair() {
        try {
            KeyPairGenerator generator = ReflectionUtils.getInstance(KeyPairGenerator.class, "ECC");
            this.privateKey = generator.genPrivateKey();
            this.publicKey = generator.genPublicKey(this.privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }
}
