package com.andreyka.crypto.api;

import lombok.Value;

import java.math.BigInteger;

@Value(staticConstructor = "create")
public class PrivateKey {
    BigInteger numberPrivateKey;
}
