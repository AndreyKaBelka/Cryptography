package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;

import java.util.HashMap;

public enum SessionIdsContainer {
    INSTANCE;

    private final HashMap<Long, Hash> container = new HashMap<>();

    public void addSessionIdForChat(final long chatId, final Hash sessionId) {
        container.put(chatId, sessionId);
    }

    public Hash getSessionIdByChatId(final long chatId) {
        return container.getOrDefault(chatId, new Hash());
    }
}
