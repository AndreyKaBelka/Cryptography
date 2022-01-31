package com.andreyka.crypto;


import com.andreyka.crypto.api.KeyPair;
import com.andreyka.crypto.api.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoServiceTest {
    private static final String TEXT_TO_CRYPT = "abcАБВ123+1234-/";
    private KeyPair myUser;
    private KeyPair otherUser;

    @BeforeEach
    public void setUp() {
        myUser = new KeyPair();
        otherUser = new KeyPair();
    }

    @Test
    public void cryptoTest() {
        BigInteger commonKey = otherUser.getPublicKey().getCommonKey(myUser.getPrivateKey());
        Pair pair = CryptoService.encrypt(commonKey, myUser.getPrivateKey(), TEXT_TO_CRYPT);

        //Сюда кладем свой публичный ключ потому-что при получении сообщения
        //другим пользователем, для него НАШ публичный ключ - это публичный ключ Другого пользователя!!!!!!!
        String decryptedText = CryptoService.decrypt(commonKey, myUser.getPublicKey(), pair);
        assertEquals(TEXT_TO_CRYPT, decryptedText);
    }
}