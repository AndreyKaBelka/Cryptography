package com.andreyka.crypto.api;

import com.andreyka.crypto.ReflectionUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyPair {
    private ECPoint publicKey;
    private BigInteger privateKey;
    private final static Logger LOGGER = Logger.getLogger(KeyPair.class.getName());

    public KeyPair() {
        try {
            KeyPairGenerator generator = ReflectionUtils.getInstance(KeyPairGenerator.class, "ECC");
            this.privateKey = generator.genPrivateKey();
            this.publicKey = generator.genPublicKey(this.privateKey);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.WARNING, "No such alg : ECC");
        }
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }
}
