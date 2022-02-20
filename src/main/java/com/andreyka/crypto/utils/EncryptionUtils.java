package com.andreyka.crypto.utils;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.containers.SessionIdsContainer;
import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.encryption.AESObject;
import com.andreyka.crypto.encryption.Base64;
import com.andreyka.crypto.exceptions.SignatureValidationException;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.*;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

@UtilityClass
public class EncryptionUtils {
    public <T> Map<Long, GroupMessage<String>> groupEncryption(T object, long chatId, User... users) {
        long[] userIds = Arrays.stream(users).mapToLong(User::getUserId).toArray();
        Map<Long, BigInteger> commonKeys = CommonKeysContainer.INSTANCE.getCommonKeysForUsers(userIds);
        long myUserId = MineInfoContainer.INSTANCE.getUserId(chatId);

        Map<Long, GroupMessage<String>> map = new TreeMap<>();
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


    public <T> void groupSignature(final long chatId, Map<Long, GroupMessage<T>> object) {
        PrivateKey myPrivateKey = MineInfoContainer.INSTANCE.getPrivateKey(chatId);
        for (GroupMessage<T> t : object.values()) {
            Signature signature = signature(forSign(t), myPrivateKey);
            t.setSignature(signature);
        }
    }

    private <T> Hash forSign(GroupMessage<T> message) {
        Hash sessionId = SessionIdsContainer.INSTANCE.getSessionIdByChatId(message.getChatId());

        return SHA2.getHash(message.getUserId() + message.getMessage().toString() + sessionId.toString());
    }

    public <T> void verifyGroupSignature(GroupMessage<T> message) {
        Signature signature = message.getSignature();
        PublicKey publicKey = ChatsContainer.INSTANCE.getUserById(message.getChatId(), message.getUserId()).getKey();

        Hash hash = forSign(message);
        if (!ECDSAService.isValid(hash, publicKey, signature)) {
            throw new SignatureValidationException("");
        }
    }

    public Signature signature(Hash object, PrivateKey privateKey) {
        return ECDSAService.getSignature(object, privateKey);
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
