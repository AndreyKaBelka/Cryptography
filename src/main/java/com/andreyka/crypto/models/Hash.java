package com.andreyka.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigInteger;

@Value
@AllArgsConstructor
public class Hash {
    String stringHash;

    public BigInteger getNumber() {
        return new BigInteger(stringHash, 16);
    }

    @Override
    public String toString() {
        return stringHash;
    }
}
