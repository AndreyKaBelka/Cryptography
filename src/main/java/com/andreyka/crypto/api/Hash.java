package com.andreyka.crypto.api;

import java.math.BigInteger;

public class Hash {
    private final BigInteger hash;

    public Hash(String hash) {
        this.hash = new BigInteger(hash, 16);
    }

    public BigInteger getNumber() {
        return hash;
    }

    public String getString() {
        return hash.toString(16);
    }
}
