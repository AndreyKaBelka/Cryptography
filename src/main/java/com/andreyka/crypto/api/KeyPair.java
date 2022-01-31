package com.andreyka.crypto.api;

import com.andreyka.crypto.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
public class KeyPair {
    private final ECPoint publicKey;
    private final BigInteger privateKey;
    private final static KeyPairGenerator generator = ReflectionUtils.getInstance(KeyPairGenerator.class, "ECC");

    public KeyPair() {
        this.privateKey = generator.genPrivateKey();
        this.publicKey = generator.genPublicKey(this.privateKey);
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }
}
