package com.andreyka.crypto.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base64ServiceTest {
    @Test
    public void codingTest() {
        assertEquals(Base64.encode("quux"), "cXV1eA==");
        assertEquals(Base64.encode("foo"), "Zm9v");
        assertEquals(Base64.encode("fo"), "Zm8=");
        assertEquals(Base64.encode("f"), "Zg==");
        assertEquals(Base64.encode(""), "");
    }
}