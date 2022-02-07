package com.andreyka.crypto.api;

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
        this.publicKey = genPublicKey(this.privateKey);
    }

    private PublicKey genPublicKey(PrivateKey privateKey) {
        Inputs[] inputs = Inputs.values();
        ECPoint g = new ECPoint(inputs);
        BigInteger d = privateKey.getNumberPrivateKey();
        ECPoint pointPubKey = g.multiply(d);
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
