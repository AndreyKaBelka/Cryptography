package com.andreyka.crypto.utils;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.containers.SessionIdsContainer;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.KeyPair;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptionUtilsTest {

    private static final String text = "абвгдеёжзasdgsadgsadg213365498:{}?><!@#$%^&*()\\+-|/";
    private static final KeyPair keyPair2 = new KeyPair();
    private BigInteger commonKey;
    private long chatId;

    @BeforeEach
    void setUp() {
        chatId = ChatsContainer.INSTANCE.initChat();
        commonKey = MineInfoContainer.INSTANCE.getPublicKey(chatId).getCommonKey(keyPair2.getPrivateKey());
        User user1 = new User(1, MineInfoContainer.INSTANCE.getPublicKey(chatId));
        User user2 = new User(2, keyPair2.getPublicKey());
        ChatsContainer.INSTANCE.addUser(chatId, user1);
        ChatsContainer.INSTANCE.addUser(chatId, user2);

        SessionIdsContainer.INSTANCE.addSessionIdForChat(chatId, SHA2.getHash(12312332));
    }

    @Test
    void encAndDecTest() {
        String encrypt = EncryptionUtils.encrypt(text, commonKey);
        String decrypt = EncryptionUtils.decrypt(encrypt, commonKey);

        assertEquals(text, decrypt);
    }

    @Test
    void signVerificationTest() {
        final GroupMessage<String> message = new GroupMessage<>(text, 1, chatId);

        EncryptionUtils.groupSignature(chatId, Map.of(2L, message));
        EncryptionUtils.verifyGroupSignature(message);
    }
}