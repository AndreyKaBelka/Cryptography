package com.andreyka.crypto.service;

import com.andreyka.crypto.CryptoOperationException;
import com.andreyka.crypto.SignatureValidationException;
import com.andreyka.crypto.data.ECPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CryptoServiceImpl implements CryptoService {
    private final Base64Service base64Service;
    private final MD5Service md5Service;
    private final ECDSAService ecdsaService;
    private final AESService aesService;


    @Autowired
    public CryptoServiceImpl(Base64Service base64Service, MD5Service md5Service, ECDSAService ecdsaService, AESService aesService) {
        this.base64Service = base64Service;
        this.md5Service = md5Service;
        this.ecdsaService = ecdsaService;
        this.aesService = aesService;
    }

    /**
     * @param commonKey      common key of two users
     * @param yourPrivateKey your private key that you can get from KeyPair
     * @param text           text you want to encrypt
     * @return pair of encrypted text and it`s signature
     */
    @Override
    public Pair encrypt(BigInteger commonKey, BigInteger yourPrivateKey, String text) throws CryptoOperationException {
        try {
            String base64EncodedMessage = base64Service.encode(text);
            String hashedBase64EncodedMessage = md5Service.getHash(base64EncodedMessage);

            ECPoint signatureForHashed = ecdsaService.getSignature(hashedBase64EncodedMessage, yourPrivateKey);

            byte[] bytes = aesService.encrypt(base64EncodedMessage, commonKey);
            byte[] base64BytesEncoded = base64Service.encode(bytes).getBytes();

            return new Pair(new String(base64BytesEncoded), signatureForHashed);
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
    @Override
    public String decrypt(BigInteger commonKey, ECPoint otherPublicKey, Pair pair) throws CryptoOperationException, SignatureValidationException {
        try {
            byte[] base64DecodedEncryptedMessage = base64Service.decode(pair.text);
            byte[] decodedByteArray = aesService.decrypt(base64DecodedEncryptedMessage, commonKey);

            String hashedBase64DecodedMessage = md5Service.getHash(new String(decodedByteArray));

            boolean isValidSignature = ecdsaService.isValid(hashedBase64DecodedMessage, otherPublicKey, pair.signature);

            if (isValidSignature) {
                return new String(base64Service.decode(new String(decodedByteArray)));
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
