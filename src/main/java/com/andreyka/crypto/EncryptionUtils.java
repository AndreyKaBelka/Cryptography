package com.andreyka.crypto;

import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.eliptic.ECDSAService;
import com.andreyka.crypto.encryption.AESObject;
import com.andreyka.crypto.encryption.Base64;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.Signature;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.map.LinkedMap;

import java.math.BigInteger;
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

    public <T> void groupSignature(Map<Long, GroupMessage<T>> object) {
        for (GroupMessage<T> t : object.values()) {
            Signature signature = signature(t.forSign());
            t.setSignature(signature);
        }
    }

    public <T> Signature signature(T object) {
        String base64 = Base64.encode(object.toString());
        Hash hash = SHA2.getHash(base64);

        return ECDSAService.getSignature(hash, MineInfoContainer.INSTANCE.getPrivateKey());
    }

    public String encrypt(String text, BigInteger commonKey) {
        String base64EncodedMessage = Base64.encode(text);

        byte[] bytes = AESObject.encrypt(base64EncodedMessage, commonKey);
        return Base64.encode(bytes);
    }
}
