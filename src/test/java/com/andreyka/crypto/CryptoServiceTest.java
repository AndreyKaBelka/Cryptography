package com.andreyka.crypto;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class CryptoServiceTest {
    private static final String TEXT_TO_CRYPT = "abcАБВ123+1234-/";
    private KeyPair myUser;
    private KeyPair otherUser;

    @Before
    public void setUp() throws Exception {
        myUser = new KeyPair();
        otherUser = new KeyPair();
    }

    @Test
    public void cryptoTest() throws CloneNotSupportedException {
        BigInteger commonKey = ECCService.getCommonKey(otherUser.getPublicKey(), myUser.getPrivateKey());
        Pair pair = CryptoService.encrypt(commonKey, myUser.getPrivateKey(), TEXT_TO_CRYPT);

        //Сюда кладем свой публичный ключ потому-что при получении сообщения
        //другим пользователем, для него НАШ публичный ключ - это публичный ключ Другого пользователя!!!!!!!
        String decryptedText = CryptoService.decrypt(commonKey, myUser.getPublicKey(), pair);
        Assert.assertEquals(TEXT_TO_CRYPT, decryptedText);
    }
}