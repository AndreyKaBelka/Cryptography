package com.andreyka.crypto.utils;

import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.encryption.AESObject;
import com.andreyka.crypto.encryption.Base64;
import com.andreyka.crypto.exceptions.CryptoOperationException;
import com.andreyka.crypto.exceptions.SignatureValidationException;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.*;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.map.LinkedMap;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@UtilityClass
public class EncryptionUtils {
    public <T> Map<Long, GroupMessage<String>> groupEncryption(T object, long chatId, User... users) {
        return groupEncryption(object, Long.valueOf(chatId), users);
    }

    public <T> Map<Long, GroupMessage<String>> groupEncryption(T object, User... users) {
        return groupEncryption(object, null, users);
    }

    private <T> Map<Long, GroupMessage<String>> groupEncryption(T object, Long chatId, User... users) {
        long[] userIds = Arrays.stream(users).mapToLong(User::getUserId).toArray();
        Map<Long, BigInteger> commonKeys = CommonKeysContainer.INSTANCE.getCommonKeysForUsers(userIds);
        long myUserId = MineInfoContainer.INSTANCE.getUserId();

        Map<Long, GroupMessage<String>> map = new LinkedMap<>();
        for (Map.Entry<Long, BigInteger> pair : commonKeys.entrySet()) {
            String encrypt = encrypt(object.toString(), pair.getValue());
            map.put(pair.getKey(), new GroupMessage<>(encrypt, myUserId, chatId));
        }
        return map;
    }

    public String decryption(final GroupMessage<String> message) {
        BigInteger commonKey = CommonKeysContainer.INSTANCE.getCommonKeyForUser(message.getUserId());
        return decrypt(message.getMessage(), commonKey);
    }


    public <T> void groupSignature(Map<Long, GroupMessage<T>> object) {
        PrivateKey myPrivateKey = MineInfoContainer.INSTANCE.getPrivateKey();
        for (GroupMessage<T> t : object.values()) {
            Signature signature = signature(t.forSign(), myPrivateKey);
            t.setSignature(signature);
        }
    }

    public <T> void verifySign(GroupMessage<T> message) {
        Signature signature = message.getSignature();
        PublicKey publicKey = MineInfoContainer.INSTANCE.getPublicKey();

        Hash hash = SHA2.getHash(message.forSign());
        if (!ECDSAService.isValid(hash, publicKey, signature)) {
            throw new SignatureValidationException("");
        }
    }

    public <T> Signature signature(T object, PrivateKey privateKey) {
        Hash hash = SHA2.getHash(object.toString());
        return ECDSAService.getSignature(hash, privateKey);
    }

    public String encrypt(String text, BigInteger commonKey) {
        String base64EncodedMessage = Base64.encode(text);

        byte[] bytes = AESObject.encrypt(base64EncodedMessage, commonKey);
        return Base64.encode(bytes);
    }

    public String decrypt(String text, BigInteger commonKey) {
        byte[] base64Decoded = Base64.decode(text);

        byte[] decrypt = AESObject.decrypt(base64Decoded, commonKey);
        return new String(Base64.decode(decrypt), StandardCharsets.UTF_8);
    }
}
