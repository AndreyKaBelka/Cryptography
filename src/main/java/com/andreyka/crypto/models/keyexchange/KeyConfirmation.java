package com.andreyka.crypto.models.keyexchange;

import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.Hash;
import lombok.ToString;
import lombok.Value;

import java.math.BigInteger;

@Value(staticConstructor = "create")
@ToString(includeFieldNames = false)
public class KeyConfirmation {
    BigInteger commonKey;
    long userId;

    public Hash hash() {
        return SHA2.getHash(this.toString());
    }
}
