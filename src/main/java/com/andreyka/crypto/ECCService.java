package com.andreyka.crypto;

import java.math.BigInteger;

public class ECCService {
    public static BigInteger getCommonKey(ECPoint publicKeyOtherUser, BigInteger yourPrivateKey) throws CloneNotSupportedException {
        return ECPoint.multiply(publicKeyOtherUser, yourPrivateKey).getX();
    }

}
