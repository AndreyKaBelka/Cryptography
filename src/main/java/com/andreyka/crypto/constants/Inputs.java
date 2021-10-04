package com.andreyka.crypto.constants;

import java.math.BigInteger;

public enum Inputs {
    GX("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16),
    GY("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16),
    P("6277101735386680763835789423207666416083908700390324961279"),
    A(String.valueOf(-3)),
    B("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16),
    N("6277101735386680763835789423176059013767194773182842284081");

    public final BigInteger value;

    Inputs(String str) {
        this.value = new BigInteger(str);
    }

    Inputs(String str, int rad) {
        this.value = new BigInteger(str, rad);
    }
}
