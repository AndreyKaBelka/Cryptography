package com.andreyka.crypto.models;

import lombok.Value;

import java.math.BigInteger;

@Value(staticConstructor = "create")
public class PublicKey {
    ECPoint pointPublicKey;

    public BigInteger getCommonKey(PrivateKey privateKey) {
        BigInteger number = privateKey.getNumberPrivateKey();
        ECPoint invoke = pointPublicKey.multiply(number);
        return invoke.getX();
    }
}
