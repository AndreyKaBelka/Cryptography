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

    public Object count(final long chatId) {
        return container.get(chatId);
    }

    public Hash getShareSecret(final long chatId, final long userId) {
        return (Hash) container.get(chatId, userId);
    }

    public static void main(String[] args) {
        SecretSharesContainer container = SecretSharesContainer.INSTANCE;
        container.addSecretShareForChatIdAndUserId(1,1,new Hash("1"));
        container.addSecretShareForChatIdAndUserId(1,2,new Hash("2"));
        container.addSecretShareForChatIdAndUserId(1,3,new Hash("3"));
        System.out.println(container.count(1));
    }
}
