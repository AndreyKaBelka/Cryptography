package com.andreyka.crypto.models;

import lombok.Value;

import java.math.BigInteger;

@Value(staticConstructor = "create")
public class PrivateKey {
    BigInteger numberPrivateKey;
}
