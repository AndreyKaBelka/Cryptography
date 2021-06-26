package com.andreyka.crypto;

import com.andreyka.crypto.exceptions.CryptoOperationException;
import com.andreyka.crypto.exceptions.SignatureValidationException;

import java.math.BigInteger;

class CryptoService {
    /**
     * @param commonKey      common key of two users
     * @param yourPrivateKey your private key that you can get from KeyPair
     * @param text           text you want to encrypt
     * @return pair of encrypted text and it`s signature
     */
    static Pair encrypt(BigInteger commonKey, BigInteger yourPrivateKey, String text) throws CryptoOperationException {
        try {
            String base64EncodedMessage = Base64.encode(text);
            String hashedBase64EncodedMessage = SHA2.getHash(base64EncodedMessage);

            ECPoint signatureForHashed = ECDSAService.getSignature(hashedBase64EncodedMessage, yourPrivateKey);

            AESObject aesObject = new AESObject(commonKey);
            byte[] bytes = aesObject.encrypt(base64EncodedMessage);
            String base64BytesEncoded = Base64.encode(bytes);

            return new Pair(base64BytesEncoded, signatureForHashed);
        } catch (Exception e) {
            throw new CryptoOperationException("Error in text encrypting!", e);
        }
    }

    /**
     * @param commonKey      common key of two users
     * @param otherPublicKey public key other user
     * @param pair           Pair pair class that contain encrypted text and signature
     * @return decrypted string
     * @throws SignatureValidationException if signature isn`t valid for this message
     */
    static String decrypt(BigInteger commonKey, ECPoint otherPublicKey, Pair pair) throws CryptoOperationException, SignatureValidationException {
        try {
            byte[] base64DecodedEncryptedMessage = Base64.decode(pair.text);
            AESObject aesObject = new AESObject(commonKey);
            byte[] decodedByteArray = aesObject.decrypt(base64DecodedEncryptedMessage);

            String hashedBase64DecodedMessage = SHA2.getHash(new String(decodedByteArray));

            boolean isValidSignature = ECDSAService.isValid(hashedBase64DecodedMessage, otherPublicKey, pair.signature);

            if (isValidSignature) {
                return new String(Base64.decode(decodedByteArray));
            } else {
                throw new SignatureValidationException("Signature isn`t valid!");
            }
        } catch (SignatureValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new CryptoOperationException("Something went wrong!", e);
        }
    }
}
