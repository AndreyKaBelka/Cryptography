package com.andreyka.crypto.service;

import com.andreyka.crypto.data.ECPoint;
import com.andreyka.crypto.data.Inputs;
import com.andreyka.crypto.data.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class KeyPairServiceImpl implements KeyPairService {
    private final ECPointMathService ecPointMathService;

    @Autowired
    public KeyPairServiceImpl(ECPointMathService ecPointMathService) {
        this.ecPointMathService = ecPointMathService;
    }

    @Override
    public KeyPair generateKeyPair() throws CloneNotSupportedException {
        KeyPair keyPair = new KeyPair();
        keyPair.setPrivate_key(genPrivateKey());
        keyPair.setPublic_key(genPublicKey(keyPair.getPrivateKey()));
        return keyPair;
    }

    private ECPoint genPublicKey(BigInteger d) throws CloneNotSupportedException {
        Inputs[] inputs = Inputs.values();
        ECPoint g = new ECPoint(inputs);
        return this.ecPointMathService.multiply(g, d);
    }

    private BigInteger genPrivateKey() {
        BigInteger key;
        SecureRandom rnd = new SecureRandom();
        do {
            key = new BigInteger(Inputs.N.value().bitLength(), rnd);
        } while (key.compareTo(Inputs.N.value().subtract(BigInteger.ONE)) >= 0);
        key = key.mod(Inputs.P.value());
        return key;
    }
}
