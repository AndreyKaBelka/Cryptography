package com.andreyka.crypto.api;

import java.math.BigInteger;

public interface KeyPairGenerator {
    ECPoint genPublicKey(BigInteger privateKey);

    BigInteger genPrivateKey();
}
