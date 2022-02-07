package com.andreyka.crypto.api;

import com.andreyka.crypto.exceptions.ECPointParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECPointParseTest {

    @Test
    public void parseValue() {
        KeyPair generatedPair = new KeyPair();
        ECPoint point = generatedPair.getPublicKey().getPointPublicKey();
        ECPoint parsedPoint = ECPoint.parseValue(point.toString());
        assertEquals(point, parsedPoint);
    }

    @Test
    public void parseValueWithException() {
        String wrongString = "123";
        Assertions.assertThrows(ECPointParseException.class, () ->  ECPoint.parseValue(wrongString));
    }
}