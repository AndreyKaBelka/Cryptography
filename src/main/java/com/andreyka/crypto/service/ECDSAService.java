package com.andreyka.crypto.service;

import com.andreyka.crypto.data.ECPoint;

import java.math.BigInteger;

public interface ECDSAService {
    ECPoint getSignature(String hash, BigInteger yourPrivateKey) throws CloneNotSupportedException;

    boolean isValid(String hash, ECPoint otherPublicKey, ECPoint signature) throws CloneNotSupportedException;
}
