package com.andreyka.crypto.utils;

import com.andreyka.crypto.models.KeyPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    private BigInteger commonKey;

    @BeforeEach
    void setUp() {
        KeyPair keyPair1 = new KeyPair();
        KeyPair keyPair2 = new KeyPair();

        commonKey = keyPair1.getPublicKey().getCommonKey(keyPair2.getPrivateKey());
    }

    @Test
    void encAndDecTest() {
        String text = "абвгдеёжзasdgsadgsadg213365498:{}?><!@#$%^&*()\\+-|/";

        String encrypt = EncryptionUtils.encrypt(text, commonKey);
        String decrypt = EncryptionUtils.decrypt(encrypt, commonKey);

        assertEquals(text, decrypt);
    }
}