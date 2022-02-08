package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

public enum SecretSharesContainer {
    INSTANCE;

    private MultiKeyMap container = MultiKeyMap.multiKeyMap(new HashedMap<>());

    public void addSecretShareForChatIdAndUserId(final long chatId, final long userId, final Hash shareSecret) {
        container.put(chatId, userId, shareSecret);
    }

    public Hash getShareSecret(final long chatId, final long userId) {
        return (Hash) container.get(chatId, userId);
    }
}
