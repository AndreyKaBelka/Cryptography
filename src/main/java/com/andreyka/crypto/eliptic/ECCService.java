package com.andreyka.crypto.eliptic;

import com.andreyka.crypto.api.ECPoint;

import java.math.BigInteger;

public class ECCService {
    /**
     * Function performs addiction two elliptic curve points
     *
     * @param point1 First elliptic point
     * @param point2 Second elliptic point
     * @return summary of two points
     */
    static ECPoint add(ECPoint point1, ECPoint point2) {
        BigInteger numerator = point1.getY().subtract(point2.getY()).mod(point1.getP());//y1-y2
        BigInteger determinate = point1.getX().subtract(point2.getX()).mod(point1.getP());//x1-x2
        determinate = getModDet(determinate, point1.getP());

        BigInteger s = numerator.multiply(determinate).mod(point1.getP());//(y1-y2)/(x1-x2)
        BigInteger x = s.pow(2).subtract(point1.getX()).subtract(point2.getX()).mod(point1.getP());//s^2-x1-x2
        BigInteger y = point1.getX().subtract(x).multiply(s).subtract(point1.getY()).mod(point1.getP());//s(x1-x3)-y1

        return new ECPoint(x, y, point1);
    }

    /**
     * Function performs doubling point
     *
     * @param point Point that need to be doubled
     * @return 2*point
     */
    static ECPoint doubleIt(ECPoint point) {
        BigInteger numerator = point.getX().pow(2).multiply(BigInteger.valueOf(3)).add(point.getA());//3x^2+a
        numerator = numerator.mod(point.getP());
        BigInteger determinate = point.getY().multiply(BigInteger.valueOf(2));//2y
        determinate = determinate.mod(point.getP());
        determinate = getModDet(determinate, point.getP());

        BigInteger s = numerator.multiply(determinate).mod(point.getP());
        BigInteger x = s.pow(2).subtract(point.getX()).subtract(point.getX()).mod(point.getP());//s^2-x-x
        BigInteger y = point.getX().subtract(x).multiply(s).subtract(point.getY()).mod(point.getP());//s(x-x3)-y

        return new ECPoint(x, y, point);
    }

    /**
     * In this function we find the mod using the equation:
     * modulus = det^(p-2) mod p
     * p-2 in the power because Euler function for prime number equal to p-1
     * The original equation is look like modulus = det^(f(p)-1) mod p
     *
     * @param det determinate, for what we need to get the mod
     * @param p   prime number
     * @return mod of 1/det
     */
    static BigInteger getModDet(BigInteger det, BigInteger p) {
        if (det.compareTo(BigInteger.ONE) == 0) {
            return BigInteger.ONE;
        }
        return det.modPow(p.subtract(BigInteger.TWO), p);
    }

    /**
     * @param point Elliptic curve point
     * @param num   the multiplier number
     * @return scalar multiply of point and number
     */
    static ECPoint multiply(ECPoint point, BigInteger num) {
        ECPoint ecPoint = point;
        ECPoint temp = (ECPoint) point.clone();
        BigInteger cnt = num.subtract(BigInteger.ONE);
        while (cnt.compareTo(BigInteger.ZERO) > 0) {
            if (cnt.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) != 0) {
                if (ecPoint.getX().compareTo(temp.getX()) == 0 && ecPoint.getY().compareTo(temp.getY()) == 0) {
                    ecPoint = doubleIt(ecPoint);
                } else {
                    ecPoint = add(ecPoint, temp);
                }
                cnt = cnt.subtract(BigInteger.ONE);
                if (cnt.compareTo(BigInteger.ZERO) == 0) {
                    break;
                }
            }
            temp = doubleIt(temp);
            cnt = cnt.divide(BigInteger.TWO);
        }
        return ecPoint;
    }
}
