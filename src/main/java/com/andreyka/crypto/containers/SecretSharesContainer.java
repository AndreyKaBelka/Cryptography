package com.andreyka.crypto.containers;

import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.observers.IObserver;
import com.andreyka.crypto.observers.SessionKeyCreationObserver;

import java.util.*;

public enum SecretSharesContainer {
    INSTANCE;

    private final Map<Long, Map<Long, Hash>> container = new HashMap<>();
    private final ArrayList<IObserver> observers = new ArrayList<>();

    {
        observers.add(new SessionKeyCreationObserver());
    }

    public void addSecretShareForChatIdAndUserId(final long chatId, final long userId, final Hash shareSecret) {
        container.computeIfAbsent(chatId, (key) -> new TreeMap<>()).put(userId, shareSecret);
        notifyAllObservers(chatId);
    }

    public int count(final long chatId) {
        return container.getOrDefault(chatId, Map.of()).values().size();
    }

    public List<Hash> getSecretShares(final long chatId) {
        return new ArrayList<>(container.get(chatId).values());
    }

    public Hash getShareSecret(final long chatId, final long userId) {
        return container.getOrDefault(chatId, Map.of()).getOrDefault(userId, null);
    }

    private void notifyAllObservers(final long chatId) {
        observers.forEach(observer -> observer.notify(chatId));
    }
}
