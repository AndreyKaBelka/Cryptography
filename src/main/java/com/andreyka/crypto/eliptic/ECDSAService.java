package com.andreyka.crypto.eliptic;

import com.andreyka.crypto.constants.Inputs;
import com.andreyka.crypto.models.*;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class ECDSAService {

    public static Signature getSignature(Hash hash, PrivateKey privateKey) {
        BigInteger r;
        KeyPair keyPair;
        BigInteger s;

        do {
            do {
                keyPair = new KeyPair();
                r = keyPair.getPublicKey().getPointPublicKey().getX().mod(Inputs.N.value);
            } while (r.compareTo(BigInteger.ZERO) == 0);

            BigInteger rda = r.multiply(privateKey.getNumberPrivateKey());
            BigInteger add = hash.getNumber().add(rda);

            BigInteger invK = keyPair.getPrivateKey().getNumberPrivateKey().modInverse(Inputs.N.value);

            s = invK.multiply(add).mod(Inputs.N.value);

        } while (s.compareTo(BigInteger.ZERO) == 0);

        ECPoint sigma = new ECPoint(r, s);
        return new Signature(sigma);
    }

    public static boolean isValid(Hash hash, PublicKey publicKey, Signature signature) {
        ECPoint sigma = signature.getPointSignature();
        BigInteger invS = sigma.getY().modInverse(Inputs.N.value);

        BigInteger u1 = invS.multiply(hash.getNumber()).mod(Inputs.N.value);
        BigInteger u2 = invS.multiply(sigma.getX()).mod(Inputs.N.value);

        ECPoint firstSummary = ECPoint.multiply(new ECPoint(), u1);
        ECPoint secondSummary = ECPoint.multiply(publicKey.getPointPublicKey(), u2);

        ECPoint pointP = ECPoint.add(firstSummary, secondSummary);

        return (sigma.getX().compareTo(pointP.getX().mod(Inputs.N.value)) == 0);
    }
}
