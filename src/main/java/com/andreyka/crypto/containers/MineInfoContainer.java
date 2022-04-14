package com.andreyka.crypto.containers;


import com.andreyka.crypto.models.KeyPair;
import com.andreyka.crypto.models.PrivateKey;
import com.andreyka.crypto.models.PublicKey;
import com.andreyka.crypto.utils.RandomUtils;

import java.util.HashMap;

public enum MineInfoContainer {
    INSTANCE;

    private final HashMap<Long, UserInfo> container = new HashMap<>();

    public PrivateKey getPrivateKey(final long chatId) {
        return getUserInfo(chatId).keyPair().getPrivateKey();
    }

    public PublicKey getPublicKey(final long chatId) {
        return getUserInfo(chatId).keyPair().getPublicKey();
    }

    public long getUserId(final long chatId) {
        return getUserInfo(chatId).userId();
    }

    public UserInfo getUserInfo(long chatId) {
        return container.computeIfAbsent(chatId, (chatId1) -> new UserInfo());
    }

    public record UserInfo(long userId, KeyPair keyPair) {
        public UserInfo() {
            this(RandomUtils.generateLong(), new KeyPair());
        }
    }
}
