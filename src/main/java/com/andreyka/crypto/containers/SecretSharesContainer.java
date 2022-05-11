package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;

import java.util.*;

public enum SecretSharesContainer {
    INSTANCE;

    private final Map<Long, Map<Long, Hash>> container = new HashMap<>();

    public void addSecretShareForChatIdAndUserId(final long chatId, final long userId, final Hash shareSecret) {
        container.computeIfAbsent(chatId, (key) -> new TreeMap<>()).put(userId, shareSecret);
    }

    public List<Hash> getSecretShares(final long chatId) {
        return new ArrayList<>(container.get(chatId).values());
    }

    public Hash getShareSecret(final long chatId, final long userId) {
        return container.getOrDefault(chatId, Map.of()).getOrDefault(userId, null);
    }

}
