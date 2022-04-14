package com.andreyka.crypto.models;

import com.andreyka.crypto.constants.Inputs;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.SecureRandom;

@Slf4j
@Value
public class KeyPair {
    PublicKey publicKey;
    PrivateKey privateKey;

    public KeyPair() {
        this.privateKey = genPrivateKey();
        this.publicKey = genPublicKey();
    }

    private PublicKey genPublicKey() {
        ECPoint g = new ECPoint();
        BigInteger d = this.privateKey.getNumberPrivateKey();
        ECPoint pointPubKey = ECPoint.multiply(g, d);
        return PublicKey.create(pointPubKey);
    }

    private PrivateKey genPrivateKey() {
        BigInteger key;
        SecureRandom rnd = new SecureRandom();
        do {
            key = new BigInteger(Inputs.N.value.bitLength(), rnd);
        } while (key.compareTo(Inputs.N.value.subtract(BigInteger.ONE)) >= 0);
        key = key.mod(Inputs.P.value);
        return PrivateKey.create(key);
    }
}
