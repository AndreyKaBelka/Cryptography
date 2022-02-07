package com.andreyka.crypto.models;

import lombok.Value;

@Value
public class Signature {
    ECPoint pointSignature;
}
