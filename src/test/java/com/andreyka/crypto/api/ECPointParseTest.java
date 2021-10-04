package com.andreyka.crypto.api;

import com.andreyka.crypto.exceptions.ECPointParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ECPointParseTest {

    @Test
    public void parseValue() {
        KeyPair generatedPair = new KeyPair();
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