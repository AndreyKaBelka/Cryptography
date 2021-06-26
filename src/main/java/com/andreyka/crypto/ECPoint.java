package com.andreyka.crypto;

import com.andreyka.crypto.exceptions.ECPointParseException;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECPoint implements Cloneable, Serializable {
    private final BigInteger a;
    private final BigInteger b;
    private final BigInteger p;
    private BigInteger x;
    private BigInteger y;

    public ECPoint(BigInteger r, BigInteger s) {
        x = r;
        y = s;
        this.a = Inputs.A.value();
        this.b = Inputs.B.value();
        this.p = Inputs.P.value();
    }

    public ECPoint(Inputs[] inputs) {
        this.x = inputs[0].value();
        this.y = inputs[1].value();
        this.a = inputs[3].value();
        this.b = inputs[4].value();
        this.p = inputs[2].value();
    }

    public ECPoint(ECPoint ecPoint) {
        this.a = ecPoint.a;
        this.b = ecPoint.b;
        this.p = ecPoint.p;
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
    }

    /**
     * @param value string like "{x_coord;r_coord}"
     * @return ECPoint parsed value
     */
    public static ECPoint parseValue(String value) {
        Pattern pattern = Pattern.compile("\\{(\\d+?);(\\d+?)\\}");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            BigInteger x = new BigInteger(matcher.group(1));
            BigInteger r = new BigInteger(matcher.group(2));
            return new ECPoint(x, r);
        } else throw new ECPointParseException(String.format("Can`t parse this string! %s", value));
    }

    /**
     * Function performs addiction two elliptic curve points
     *
     * @param point1 First elliptic point
     * @param point2 Second elliptic point
     * @return summary of two points
     */
    static ECPoint add(ECPoint point1, ECPoint point2) {
        ECPoint point3 = new ECPoint(point1);

        BigInteger numerator = point1.getY().subtract(point2.getY()).mod(point3.p);//y1-y2
        BigInteger determinate = point1.getX().subtract(point2.getX()).mod(point3.p);//x1-x2
        determinate = getModDet(determinate, point3.p);

        BigInteger s = numerator.multiply(determinate).mod(point3.p);//(y1-y2)/(x1-x2)
        point3.x = s.pow(2).subtract(point1.getX()).subtract(point2.getX()).mod(point3.p);//s^2-x1-x2
        point3.y = point1.getX().subtract(point3.getX()).multiply(s).subtract(point1.getY()).mod(point3.p);//s(x1-x3)-y1

        return point3;
    }

    /**
     * Function performs doubling point
     *
     * @param point Point that need to be doubled
     * @return 2*point
     */
    private static ECPoint Double(ECPoint point) {
        ECPoint ecPoint = new ECPoint(point);

        BigInteger numerator = point.x.pow(2).multiply(BigInteger.valueOf(3)).add(point.a);//3x^2+a
        numerator = numerator.mod(ecPoint.p);
        BigInteger determinate = point.getY().multiply(BigInteger.valueOf(2));//2y
        determinate = determinate.mod(ecPoint.p);
        determinate = getModDet(determinate, ecPoint.p);

        BigInteger s = numerator.multiply(determinate).mod(ecPoint.p);
        ecPoint.x = s.pow(2).subtract(point.getX()).subtract(point.getX()).mod(ecPoint.p);//s^2-x-x
        ecPoint.y = point.getX().subtract(ecPoint.getX()).multiply(s).subtract(point.getY()).mod(ecPoint.p);//s(x-x3)-y

        return ecPoint;
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
    private static BigInteger getModDet(BigInteger det, BigInteger p) {
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
    static ECPoint multiply(ECPoint point, BigInteger num) throws CloneNotSupportedException {
        ECPoint ecPoint = point;
        ECPoint temp = (ECPoint) point.clone();
        BigInteger cnt = num.subtract(BigInteger.ONE);
        while (cnt.compareTo(BigInteger.ZERO) > 0) {
            if (cnt.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) != 0) {
                if (ecPoint.getX().compareTo(temp.getX()) == 0 && ecPoint.getY().compareTo(temp.getY()) == 0) {
                    ecPoint = Double(ecPoint);
                } else {
                    ecPoint = add(ecPoint, temp);
                }
                cnt = cnt.subtract(BigInteger.ONE);
                if (cnt.compareTo(BigInteger.ZERO) == 0) {
                    break;
                }
            }
            temp = Double(temp);
            cnt = cnt.divide(BigInteger.TWO);
        }
        return ecPoint;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" + this.x + ";" + this.y + "}";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ECPoint ecPoint = (ECPoint) o;
        return x.equals(ecPoint.x) && y.equals(ecPoint.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
