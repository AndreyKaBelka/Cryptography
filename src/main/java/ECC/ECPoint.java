package ECC;

import java.io.Serializable;
import java.math.BigInteger;

public class ECPoint implements Cloneable, Serializable {
    public BigInteger x;
    public BigInteger y;
    private BigInteger a;
    private BigInteger b;
    private BigInteger p;

    public ECPoint() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
        this.a = BigInteger.ZERO;
        this.b = BigInteger.ZERO;
        this.p = BigInteger.ZERO;
    }

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

    /**
     * Function performs addiction two elliptic curve points
     *
     * @param point1 First elliptic point
     * @param point2 Second elliptic point
     * @return summary of two points
     */
    public static ECPoint add(ECPoint point1, ECPoint point2) {
        ECPoint point3 = new ECPoint();
        point3.a = point1.a;
        point3.b = point1.b;
        point3.p = point1.p;

        BigInteger numerator = point1.y.subtract(point2.y).mod(point3.p);//y1-y2
        BigInteger determinate = point1.x.subtract(point2.x).mod(point3.p);//x1-x2
        determinate = getModDet(determinate, point3.p);

        BigInteger s = numerator.multiply(determinate).mod(point3.p);//(y1-y2)/(x1-x2)
        point3.x = s.pow(2).subtract(point1.x).subtract(point2.x).mod(point3.p);//s^2-x1-x2
        point3.y = point1.x.subtract(point3.x).multiply(s).subtract(point1.y).mod(point3.p);//s(x1-x3)-y1

        return point3;
    }

    /**
     * Function performs doubling point
     *
     * @param point Point that need to be doubled
     * @return 2*point
     */
    private static ECPoint Double(ECPoint point) {
        ECPoint ecPoint = new ECPoint();
        ecPoint.a = point.a;
        ecPoint.b = point.b;
        ecPoint.p = point.p;

        BigInteger numerator = point.x.pow(2).multiply(BigInteger.valueOf(3)).add(point.a);//3x^2+a
        numerator = numerator.mod(ecPoint.p);
        BigInteger determinate = point.y.multiply(BigInteger.valueOf(2));//2y
        determinate = determinate.mod(ecPoint.p);
        determinate = getModDet(determinate, ecPoint.p);

        BigInteger s = numerator.multiply(determinate).mod(ecPoint.p);
        ecPoint.x = s.pow(2).subtract(point.x).subtract(point.x).mod(ecPoint.p);//s^2-x-x
        ecPoint.y = point.x.subtract(ecPoint.x).multiply(s).subtract(point.y).mod(ecPoint.p);//s(x-x3)-y

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
    public static ECPoint multiply(ECPoint point, BigInteger num) throws CloneNotSupportedException {
        ECPoint ecPoint = point;
        ECPoint temp = (ECPoint) point.clone();
        BigInteger cnt = num.subtract(BigInteger.ONE);
        while (cnt.compareTo(BigInteger.ZERO) > 0) {
            if (cnt.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) != 0) {
                if (ecPoint.x.compareTo(temp.x) == 0 && ecPoint.y.compareTo(temp.y) == 0) {
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

    @Override
    public String toString() {
        return "{" + this.x + ";" + this.y + "}";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
