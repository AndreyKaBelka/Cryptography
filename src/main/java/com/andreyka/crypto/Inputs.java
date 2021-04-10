package com.andreyka.crypto;

import java.math.BigInteger;

enum Inputs {
    GX {
        BigInteger value() {
            return new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
        }
    },
    GY {
        BigInteger value() {
            return new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16);
        }
    },
    P {
        BigInteger value() {
            return new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        }
    },
    A {
        BigInteger value() {
            return new BigInteger(String.valueOf(-3));
        }
    },
    B {
        BigInteger value() {
            return new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
        }
    },
    N {
        BigInteger value() {
            return new BigInteger("6277101735386680763835789423176059013767194773182842284081");
        }
    };

    abstract BigInteger value();
}
