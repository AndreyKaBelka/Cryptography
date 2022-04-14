package com.andreyka.crypto.models;

import com.andreyka.crypto.exceptions.ECPointParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECPointTest {

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

    @Test
    void testAddition() {
        ECPoint ecPoint1 = new ECPoint(new BigInteger("16"), new BigInteger("20"));
        ECPoint ecPoint2 = new ECPoint(new BigInteger("41"), new BigInteger("120"));

        ECPoint ecPointVal = ECPoint.add(ecPoint1, ecPoint2);
        System.out.println(ecPointVal);
    }
}