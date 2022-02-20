package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;

import java.util.HashMap;

public enum SessionKeysContainer {
    INSTANCE;

    private final HashMap<Long, Hash> container = new HashMap<>();

    public void addSessionKey(final long chatId, final Hash sessionKey) {
        container.put(chatId, sessionKey);
    }

    public Hash getSessionKey(final long chatId) {
        return container.get(chatId);
    }
}
