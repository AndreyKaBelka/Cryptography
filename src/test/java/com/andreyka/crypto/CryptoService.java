package com.andreyka.crypto;

import com.andreyka.crypto.models.*;
import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.encryption.AESObject;
import com.andreyka.crypto.encryption.Base64;
import com.andreyka.crypto.exceptions.CryptoOperationException;
import com.andreyka.crypto.exceptions.SignatureValidationException;
import com.andreyka.crypto.hashes.SHA2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

class CryptoService {
    /**
     * @param commonKey      common key of two users
     * @param yourPrivateKey your private key that you can get from KeyPair
     * @param text           text you want to encrypt
     * @return pair of encrypted text and it`s signature
     */
    static Pair encrypt(BigInteger commonKey, PrivateKey yourPrivateKey, String text) throws CryptoOperationException {
        try {
            String base64EncodedMessage = Base64.encode(text);
            Hash hashedBase64EncodedMessage = SHA2.getHash(base64EncodedMessage);

            Signature signatureForHashed = ECDSAService.getSignature(hashedBase64EncodedMessage, yourPrivateKey);

            byte[] bytes = AESObject.encrypt(base64EncodedMessage, commonKey);
            String base64BytesEncoded = Base64.encode(bytes);

            return new Pair(base64BytesEncoded, signatureForHashed);
        } catch (Exception e) {
            throw new CryptoOperationException("Error in text encrypting!", e);
        }
    }

    /**
     * @param commonKey      common key of two users
     * @param otherPublicKey public key other user
     * @param pair           Pair class that contain encrypted text and signature
     * @return decrypted string
     * @throws SignatureValidationException if signature isn`t valid for this message
     */
    static String decrypt(BigInteger commonKey, PublicKey otherPublicKey, Pair pair) throws CryptoOperationException, SignatureValidationException {
        try {
            byte[] base64DecodedEncryptedMessage = Base64.decode(pair.text);
            byte[] decodedByteArray = AESObject.decrypt(base64DecodedEncryptedMessage, commonKey);

            Hash hashedBase64DecodedMessage = SHA2.getHash(new String(decodedByteArray));

            boolean isValidSignature = ECDSAService.isValid(hashedBase64DecodedMessage, otherPublicKey, pair.signature);

            if (isValidSignature) {
                return new String(Base64.decode(decodedByteArray), StandardCharsets.UTF_8);
            } else {
                throw new SignatureValidationException("Signature isn`t valid!");
            }
        } catch (Exception e) {
            throw new CryptoOperationException("Something went wrong!", e);
        }
    }
}
