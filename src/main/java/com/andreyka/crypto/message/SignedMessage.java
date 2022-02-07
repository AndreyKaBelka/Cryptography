package com.andreyka.crypto.message;

import com.andreyka.crypto.api.Hash;
import com.andreyka.crypto.api.PrivateKey;
import com.andreyka.crypto.api.Signature;
import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.hashes.SHA2;
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
        Signature signature = ECDSAService.getSignature(hash, privateKey);
        return new SignedMessage(extendedMessage, signature);
    }
}
