package KeyPair;

import ECC.ECPoint;
import ECC.Inputs;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KEYPair {
    private ECPoint public_key;
    private BigInteger private_key;

    private KEYPair() {
        this.public_key = null;
        this.private_key = BigInteger.ZERO;
    }

    public BigInteger getPrivate_key() {
        return private_key;
    }

    public ECPoint getPublic_key() {
        return public_key;
    }

    public static KEYPair generateKeyPair() throws CloneNotSupportedException {
        KEYPair keyPair = new KEYPair();
        keyPair.private_key = genPrivateKey();
        keyPair.public_key = genPublicKey(keyPair.private_key);
        return keyPair;
    }

    private static ECPoint genPublicKey(BigInteger d) throws CloneNotSupportedException {
        Inputs[] inputs = Inputs.values();
        ECPoint g = new ECPoint(inputs);
        return ECPoint.multiply(g, d);
    }

    private static BigInteger genPrivateKey(){
        BigInteger key;
        SecureRandom rnd = new SecureRandom();
        do {
            key = new BigInteger(Inputs.N.value().bitLength(), rnd);
        } while (key.compareTo(Inputs.N.value().subtract(BigInteger.ONE)) >= 0);
        key = key.mod(Inputs.P.value());
        return key;
    }

    @Override
    public String toString() {
        return "Private key: " + private_key + "\nPublic key: " + public_key;
    }
}
