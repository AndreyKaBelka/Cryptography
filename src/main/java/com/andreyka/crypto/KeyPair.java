package com.andreyka.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {
    private ECPoint publicKey;
    private BigInteger privateKey;

    public KeyPair() {
        this.publicKey = null;
        this.privateKey = BigInteger.ZERO;
    }

    public static KeyPair generateKeyPair() throws CloneNotSupportedException {
        KeyPair keyPair = new KeyPair();
        keyPair.setPrivateKey(keyPair.genPrivateKey());
        keyPair.setPublicKey(keyPair.genPublicKey(keyPair.getPrivateKey()));
        return keyPair;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }

    void setPublicKey(ECPoint publicKey) {
        this.publicKey = publicKey;
    }

    private ECPoint genPublicKey(BigInteger d) throws CloneNotSupportedException {
        Inputs[] inputs = Inputs.values();
        ECPoint g = new ECPoint(inputs);
        return ECPoint.multiply(g, d);
    }

    private BigInteger genPrivateKey() {
        BigInteger key;
        SecureRandom rnd = new SecureRandom();
        do {
            key = new BigInteger(Inputs.N.value().bitLength(), rnd);
        } while (key.compareTo(Inputs.N.value().subtract(BigInteger.ONE)) >= 0);
        key = key.mod(Inputs.P.value());
        return key;
    }

    @Override
    public String toString() {
        return "Private key: " + privateKey + "\nPublic key: " + publicKey;
    }
}
