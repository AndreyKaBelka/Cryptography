package com.andreyka.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {
    private final ECPoint publicKey;
    private final BigInteger privateKey;

    public KeyPair() throws CloneNotSupportedException {
        this.privateKey = genPrivateKey();
        this.publicKey = genPublicKey(this.privateKey);
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
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
