package Client;

import ECC.ECPoint;
import KeyPair.KeyPair;

import java.math.BigInteger;

public class ClientData {
    private static BigInteger PRIVATE_KEY;
    private static ECPoint PUBLIC_KEY;
    private static String USERNAME = null;
    private static BigInteger COMMON_KEY;
    private static ECPoint PUBLIC_KEY_SERVER;

    static {
        KeyPair keyPair = null;
        try {
            keyPair = KeyPair.generateKeyPair();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        assert keyPair != null;
        PRIVATE_KEY = keyPair.getPrivate_key();
        PUBLIC_KEY = keyPair.getPublic_key();
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        ClientData.USERNAME = USERNAME;
    }

    static BigInteger getPrivateKey() {
        return PRIVATE_KEY;
    }

    static ECPoint getPublicKey() {
        return PUBLIC_KEY;
    }

    static BigInteger getCommonKey() {
        return COMMON_KEY;
    }

    static void setCommonKey(ECPoint publicKeyServer) throws CloneNotSupportedException {
        COMMON_KEY = ECC.ECC.getCommonKey(publicKeyServer, PRIVATE_KEY);
    }

    static ECPoint getPublicKeyServer() {
        return PUBLIC_KEY_SERVER;
    }

    static void setPublicKeyServer(ECPoint publicKeyServer) {
        PUBLIC_KEY_SERVER = publicKeyServer;
    }
}
