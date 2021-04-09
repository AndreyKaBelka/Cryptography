package com.andreyka.crypto.service;

import com.andreyka.crypto.data.Word;

import java.math.BigInteger;

public interface AESService {
    Word[] setWordsByKey(BigInteger key);

    byte[] encrypt(String text, BigInteger commonKey);

    byte[] decrypt(byte[] text, BigInteger commonKey);
}
