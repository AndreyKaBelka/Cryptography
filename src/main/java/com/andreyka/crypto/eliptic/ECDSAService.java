package com.andreyka.crypto.eliptic;

import com.andreyka.crypto.api.ECPoint;
import com.andreyka.crypto.api.Hash;
import com.andreyka.crypto.api.KeyPair;
import com.andreyka.crypto.constants.Inputs;

import java.math.BigInteger;

public class ECDSAService {

    public static ECPoint getSignature(Hash hash, BigInteger yourPrivateKey) {
        BigInteger r;
        KeyPair keyPair;
        BigInteger s;

        do {
            do {
                keyPair = new KeyPair();
                r = keyPair.getPublicKey().getX().mod(Inputs.N.value);
            } while (r.compareTo(BigInteger.ZERO) == 0);

            BigInteger rda = r.multiply(yourPrivateKey);
            BigInteger add = hash.getNumber().add(rda);

            BigInteger inv_k = keyPair.getPrivateKey().modInverse(Inputs.N.value);

            s = inv_k.multiply(add).mod(Inputs.N.value);

        } while (s.compareTo(BigInteger.ZERO) == 0);

        return new ECPoint(r, s);
    }

    public static boolean isValid(Hash hash, ECPoint otherPublicKey, ECPoint signature) {
        BigInteger inv_s = signature.getY().modInverse(Inputs.N.value);

        BigInteger u1 = inv_s.multiply(hash.getNumber()).mod(Inputs.N.value);
        BigInteger u2 = inv_s.multiply(signature.getX()).mod(Inputs.N.value);

        ECPoint first_summary = ECCService.multiply(new ECPoint(Inputs.values()), u1);
        ECPoint second_summary = ECCService.multiply(otherPublicKey, u2);

        ECPoint pointP = ECCService.add(first_summary, second_summary);

        return (signature.getX().compareTo(pointP.getX().mod(Inputs.N.value)) == 0);
    }
}
