package com.andreyka.crypto.service;


import com.andreyka.crypto.BaseTest;
import com.andreyka.crypto.data.KeyPair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class CryptoServiceTest extends BaseTest {
    private KeyPair myUser;
    private KeyPair otherUser;
    private static final String TEXT_TO_CRYPT = "abcАБВ123+1234-/";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myUser = keyPairService.generateKeyPair();
        otherUser = keyPairService.generateKeyPair();
    }

    @Test
    public void cryptoTest() throws CloneNotSupportedException {
        BigInteger commonKey = eccService.getCommonKey(otherUser.getPublicKey(), myUser.getPrivateKey());
        CryptoServiceImpl.Pair pair = cryptoService.encrypt(commonKey, myUser.getPrivateKey(), TEXT_TO_CRYPT);

        //Сюда кладем свой публичный ключ потому-что при получении сообщения
        //другим пользователем, для него НАШ публичный ключ - это публичный ключ Другого пользователя!!!!!!!
        String decryptedText = cryptoService.decrypt(commonKey, myUser.getPublicKey(), pair);
        Assert.assertEquals(TEXT_TO_CRYPT, decryptedText);
    }
}