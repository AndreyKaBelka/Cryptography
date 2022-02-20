package com.andreyka.crypto.models;

import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.utils.EncryptionUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignedMessage {
    ExtendedMessage message;

    Signature signature;

    public static SignedMessage signMessage(final ExtendedMessage extendedMessage, final PrivateKey privateKey) {
        Hash hash = SHA2.getHash(extendedMessage.toString());
        Signature signature = EncryptionUtils.signature(hash, privateKey);
        return new SignedMessage(extendedMessage, signature);
    }
}
