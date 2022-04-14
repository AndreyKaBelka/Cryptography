package com.andreyka.crypto.models.keyexchange;

import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.Hash;
import lombok.Value;

import java.math.BigInteger;

@Value(staticConstructor = "create")
public class SecretShare {
    BigInteger commonKey;
    Hash sessionId;

    public Hash generate() {
        return SHA2.getHash(this.toString());
    }
}
