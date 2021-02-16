package com.andreyka.crypto.data;

import java.io.Serializable;
import java.math.BigInteger;

public class ECPoint implements Cloneable, Serializable {
    private BigInteger x;
    private BigInteger y;
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

    public ECPoint(ECPoint ecPoint) {
        this.a = ecPoint.a;
        this.b = ecPoint.b;
        this.p = ecPoint.p;
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    @Override
    public String toString() {
        return "{" + this.x + ";" + this.y + "}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
