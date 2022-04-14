package com.andreyka.crypto.models;

import lombok.Data;

import java.math.BigInteger;

@Data
public class User {
    private final long userId;
    private final PublicKey key;

    public long bitSum() {
        return key.getPointPublicKey().getX().or(BigInteger.valueOf(userId)).longValueExact();
    }
}
