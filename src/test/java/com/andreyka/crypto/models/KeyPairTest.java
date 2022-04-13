package com.andreyka.crypto.models;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TimingExtension.class)
public class KeyPairTest {
    private final static KeyPair keyPair1 = new KeyPair();
    private final static KeyPair keyPair2 = new KeyPair();

    @Test
    public void testCommonKey() {
        BigInteger commonKey1 = keyPair1.getPublicKey().getCommonKey(keyPair2.getPrivateKey());
        BigInteger commonKey2 = keyPair2.getPublicKey().getCommonKey(keyPair1.getPrivateKey());
        assertEquals(commonKey2, commonKey1);
    }

    @RepeatedTest(1000)
    public void test_TimeOfCommonKeyCreating() {
        keyPair1.getPublicKey().getCommonKey(keyPair2.getPrivateKey());
    }
}
