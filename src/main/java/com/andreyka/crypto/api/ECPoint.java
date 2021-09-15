package com.andreyka.crypto.api;

import com.andreyka.crypto.ReflectionUtils;
import com.andreyka.crypto.constants.Inputs;
import com.andreyka.crypto.eliptic.ECCService;
import com.andreyka.crypto.exceptions.CryptoOperationException;
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
    private final BigInteger x;
    private final BigInteger y;

    public ECPoint(BigInteger r, BigInteger s) {
        x = r;
        y = s;
        this.a = Inputs.A.value;
        this.b = Inputs.B.value;
        this.p = Inputs.P.value;
    }

    public ECPoint(Inputs[] inputs) {
        this.x = inputs[0].value;
        this.y = inputs[1].value;
        this.p = inputs[2].value;
        this.a = inputs[3].value;
        this.b = inputs[4].value;
    }

    public ECPoint(BigInteger x, BigInteger y, ECPoint ecPoint) {
        this.a = ecPoint.a;
        this.b = ecPoint.b;
        this.p = ecPoint.p;
        this.x = x;
        this.y = y;
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
        } else throw new ECPointParseException(String.format("Can`t parse this string: %s", value));
    }

    public BigInteger getCommonKey(BigInteger yourPrivateKey) throws NoSuchMethodException {
        ECPoint invoke = (ECPoint) ReflectionUtils.getMethodResult(ECCService.class, "multiply", this, yourPrivateKey);
        return invoke.getX();
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
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
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CryptoOperationException("Try again later", e.getCause());
        }
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
