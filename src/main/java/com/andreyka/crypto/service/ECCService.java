package com.andreyka.crypto.service;

import com.andreyka.crypto.data.ECPoint;

import java.math.BigInteger;

public interface ECCService {
    BigInteger getCommonKey(ECPoint publicKeyOtherUser, BigInteger yourPrivateKey) throws CloneNotSupportedException;
}
