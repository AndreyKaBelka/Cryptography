package com.andreyka.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigInteger;

@Value
@AllArgsConstructor
public class Hash {
    String stringHash;

    public Hash() {
        this.stringHash = "0";
    }

    public BigInteger getNumber() {
        return new BigInteger(stringHash, 16);
    }

    public Hash xor(Hash otherHash) {
        BigInteger xorRes = this.getNumber().xor(otherHash.getNumber());
        return new Hash(xorRes.toString(16));
    }

    @Override
    public String toString() {
        return stringHash;
    }
}
