package com.andreyka.crypto;

import java.math.BigInteger;

public class ECDSAService {

    public static ECPoint getSignature(String hash, BigInteger yourPrivateKey) throws CloneNotSupportedException {
        BigInteger r;
        KeyPair keyPair;
        BigInteger s;
        BigInteger _hash = new BigInteger(hash, 16);

        do {
            do {
                keyPair = new KeyPair();
                r = keyPair.getPublicKey().getX().mod(Inputs.N.value());
            } while (r.compareTo(BigInteger.ZERO) == 0);

            BigInteger rda = r.multiply(yourPrivateKey);
            BigInteger add = _hash.add(rda);

            BigInteger inv_k = keyPair.getPrivateKey().modInverse(Inputs.N.value());

            s = inv_k.multiply(add).mod(Inputs.N.value());

        } while (s.compareTo(BigInteger.ZERO) == 0);

        return new ECPoint(r, s);
    }

    public static boolean isValid(String hash, ECPoint otherPublicKey, ECPoint signature) throws CloneNotSupportedException {
        BigInteger inv_s = signature.getY().modInverse(Inputs.N.value());
        BigInteger _hash = new BigInteger(hash, 16);

        BigInteger u1 = inv_s.multiply(_hash).mod(Inputs.N.value());
        BigInteger u2 = inv_s.multiply(signature.getX()).mod(Inputs.N.value());

        ECPoint first_summary = ECPoint.multiply(new ECPoint(Inputs.values()), u1);
        ECPoint second_summary = ECPoint.multiply(otherPublicKey, u2);

        ECPoint pointP = ECPoint.add(first_summary, second_summary);

        return (signature.getX().compareTo(pointP.getX().mod(Inputs.N.value())) == 0);
    }
}
