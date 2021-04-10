package com.andreyka.crypto;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class CryptoServiceTest {
    private KeyPair myUser;
    private KeyPair otherUser;
    private static final String TEXT_TO_CRYPT = "abcАБВ123+1234-/";

    @Before
    public void setUp() throws Exception {
        myUser = KeyPair.generateKeyPair();
        otherUser = KeyPair.generateKeyPair();
    }

    @Test
    public void cryptoTest() throws CloneNotSupportedException {
        BigInteger commonKey = ECCService.getCommonKey(otherUser.getPublicKey(), myUser.getPrivateKey());
        CryptoService.Pair pair = CryptoService.encrypt(commonKey, myUser.getPrivateKey(), TEXT_TO_CRYPT);

        //Сюда кладем свой публичный ключ потому-что при получении сообщения
        //другим пользователем, для него НАШ публичный ключ - это публичный ключ Другого пользователя!!!!!!!
        String decryptedText = CryptoService.decrypt(commonKey, myUser.getPublicKey(), pair);
        Assert.assertEquals(TEXT_TO_CRYPT, decryptedText);
    }
}