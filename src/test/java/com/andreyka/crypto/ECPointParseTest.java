package com.andreyka.crypto;

import org.junit.Test;

import static org.junit.Assert.*;

public class ECPointParseTest {

    @Test
    public void parseValue() throws CloneNotSupportedException {
        KeyPair generatedPair = KeyPair.generateKeyPair();
        ECPoint point = generatedPair.getPublicKey();
        ECPoint parsedPoint = ECPoint.parseValue(point.toString());
        assertEquals(point, parsedPoint);
    }

    @Test(expected = ECPointParseException.class)
    public void parseValueWithException() {
        String wrongString = "123";
        ECPoint.parseValue(wrongString);
    }
}