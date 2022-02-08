package com.andreyka.crypto.containers;

import lombok.Getter;
import org.apache.commons.collections4.map.LinkedMap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum CommonKeysContainer {
    INSTANCE;

    private static final HashMap<Long, BigInteger> container = new HashMap<>();

    public BigInteger getCommonKeyForUser(final long userId) {
        return container.getOrDefault(userId, BigInteger.ZERO);
    }

    public void addCommonKeyForUser(final long userId, final BigInteger commonKey) {
        container.put(userId, commonKey);
    }

    public Map<Long, BigInteger> getCommonKeysForUsers(final long... userIds) {
        Map<Long, BigInteger> res = new LinkedMap<>();
        for (long userId : userIds) {
            res.put(userId, container.get(userId));
        }

        return res;
    }
}
