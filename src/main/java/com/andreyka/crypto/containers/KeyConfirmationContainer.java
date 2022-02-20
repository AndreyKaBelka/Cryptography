package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;

import java.util.HashMap;

public enum KeyConfirmationContainer {
    INSTANCE;

    private final HashMap<Long, Hash> container = new HashMap<>();

    public void addKeyConfirmationForUser(final long userId, final Hash keyConfirmation) {
        container.put(userId, keyConfirmation);
    }

    public Hash getKeyConfirmationForUser(final long userId) {
        return container.getOrDefault(userId, new Hash());
    }
}
