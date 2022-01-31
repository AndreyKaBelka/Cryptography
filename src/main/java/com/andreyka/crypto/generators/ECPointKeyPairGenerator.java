package com.andreyka.crypto.generators;

import com.andreyka.crypto.Algorithm;
import com.andreyka.crypto.ReflectionUtils;
import com.andreyka.crypto.api.ECPoint;
import com.andreyka.crypto.api.KeyPairGenerator;
import com.andreyka.crypto.constants.Inputs;
import com.andreyka.crypto.eliptic.ECCService;

import java.math.BigInteger;
import java.security.SecureRandom;

@Algorithm("ECC")
public class ECPointKeyPairGenerator implements KeyPairGenerator {
    public ECPoint genPublicKey(BigInteger d) {
        Inputs[] inputs = Inputs.values();
        ECPoint g = new ECPoint(inputs);
        return (ECPoint) ReflectionUtils.getMethodResult(ECCService.class, "multiply", g, d);
    }

    public BigInteger genPrivateKey() {
        BigInteger key;
        SecureRandom rnd = new SecureRandom();
        do {
            key = new BigInteger(Inputs.N.value.bitLength(), rnd);
        } while (key.compareTo(Inputs.N.value.subtract(BigInteger.ONE)) >= 0);
        key = key.mod(Inputs.P.value);
        return key;
    }
}
