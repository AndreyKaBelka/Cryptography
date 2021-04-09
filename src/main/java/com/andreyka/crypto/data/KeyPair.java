package com.andreyka.crypto.data;

import java.math.BigInteger;

public class KeyPair {
    private ECPoint public_key;
    private BigInteger private_key;

    public KeyPair() {
        this.public_key = null;
        this.private_key = BigInteger.ZERO;
    }

    public BigInteger getPrivateKey() {
        return private_key;
    }

    public ECPoint getPublicKey() {
        return public_key;
    }

    public void setPublic_key(ECPoint public_key) {
        this.public_key = public_key;
    }

    public void setPrivate_key(BigInteger private_key) {
        this.private_key = private_key;
    }

    @Override
    public String toString() {
        return "Private key: " + private_key + "\nPublic key: " + public_key;
    }
}
