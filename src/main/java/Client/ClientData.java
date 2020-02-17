package Client;

import ECC.ECPoint;
import KeyPair.KEYPair;

import java.math.BigInteger;
import java.util.Scanner;

class ClientData {
    private static BigInteger PRIVATE_KEY;
    private static ECPoint PUBLIC_KEY;
    private static String USERNAME = null;
    private static BigInteger COMMON_KEY;
    private static ECPoint PUBLIC_KEY_SERVER;

    static {
        KEYPair keyPair = null;
        try {
            keyPair = KEYPair.generateKeyPair();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        assert keyPair != null;
        PRIVATE_KEY = keyPair.getPrivate_key();
        PUBLIC_KEY = keyPair.getPublic_key();
    }

    static String getUSERNAME() {
        if (USERNAME == null) {
            Scanner in = new Scanner(System.in);
            USERNAME = in.nextLine();
        }
        return USERNAME;
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
