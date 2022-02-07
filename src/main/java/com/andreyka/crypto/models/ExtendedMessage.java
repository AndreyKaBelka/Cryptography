package com.andreyka.crypto.models;

import lombok.Data;
import lombok.ToString;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Data(staticConstructor = "create")
@ToString(includeFieldNames = false)
public class ExtendedMessage {
    private final Message message;
    private final Hash parentHash;
    private final Hash chainHash;
    private final long parentId;
    private final long ownSeqNum;

    private byte[] salt = new byte[16];

    {
        try {
            SecureRandom random = SecureRandom.getInstance("DRBG");
            random.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
