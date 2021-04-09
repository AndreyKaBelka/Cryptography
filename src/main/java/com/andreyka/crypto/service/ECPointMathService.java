package com.andreyka.crypto.service;

import com.andreyka.crypto.data.ECPoint;

import java.math.BigInteger;

public interface ECPointMathService {
    ECPoint add(ECPoint point1, ECPoint point2);

    ECPoint multiply(ECPoint point, BigInteger num) throws CloneNotSupportedException;
}
