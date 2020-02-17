package ECDSA;

import ECC.ECPoint;
import ECC.Inputs;
import KeyPair.KEYPair;

import java.math.BigInteger;

public class ECDSA {
    public static ECPoint getSignature(String hash, BigInteger your_private_key) throws CloneNotSupportedException {
        BigInteger r;
        KEYPair keyPair;
        BigInteger s;
        BigInteger _hash = new BigInteger(hash, 16);

        do {
            do {
                keyPair = KEYPair.generateKeyPair();
                r = keyPair.getPublic_key().x.mod(Inputs.N.value());
            } while (r.compareTo(BigInteger.ZERO) == 0);

            BigInteger rda = r.multiply(your_private_key);
            BigInteger add = _hash.add(rda);

            BigInteger inv_k = keyPair.getPrivate_key().modInverse(Inputs.N.value());

            s = inv_k.multiply(add).mod(Inputs.N.value());

        } while (s.compareTo(BigInteger.ZERO) == 0);

        return new ECPoint(r, s);
    }

    public static boolean isValid(String hash, ECPoint other_public_key, ECPoint signature) throws CloneNotSupportedException {
        BigInteger inv_s = signature.y.modInverse(Inputs.N.value());
        BigInteger _hash = new BigInteger(hash, 16);

        BigInteger u1 = inv_s.multiply(_hash).mod(Inputs.N.value());
        BigInteger u2 = inv_s.multiply(signature.x).mod(Inputs.N.value());

        ECPoint first_summary = ECPoint.multiply(new ECPoint(Inputs.values()), u1);
        ECPoint second_summary = ECPoint.multiply(other_public_key, u2);

        ECPoint pointP = ECPoint.add(first_summary, second_summary);

        return (signature.x.compareTo(pointP.x.mod(Inputs.N.value())) == 0);
    }
}
