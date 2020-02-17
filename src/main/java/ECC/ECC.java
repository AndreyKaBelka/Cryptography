package ECC;

import java.math.BigInteger;

public class ECC {
    public static BigInteger getCommonKey(ECPoint publicKeyOtherUser, BigInteger yourPrivateKey) throws CloneNotSupportedException {
        return ECPoint.multiply(publicKeyOtherUser, yourPrivateKey).x;
    }

}
