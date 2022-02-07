package com.andreyka.crypto.models;

import lombok.Value;

import java.math.BigInteger;

@Value
public class Hash {
    String stringHash;

    public BigInteger getNumber() {
        return new BigInteger(stringHash, 16);
    }
}
