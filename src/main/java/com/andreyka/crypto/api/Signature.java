package com.andreyka.crypto.api;

import lombok.Value;

@Value
public class Signature {
    ECPoint pointSignature;
}
