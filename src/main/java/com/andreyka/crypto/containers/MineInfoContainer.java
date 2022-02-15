package com.andreyka.crypto.containers;


import com.andreyka.crypto.models.KeyPair;
import com.andreyka.crypto.models.PrivateKey;
import com.andreyka.crypto.models.PublicKey;

public enum MineInfoContainer {
    INSTANCE;

    private KeyPair keyPair;
    private long userID;

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivateKey();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublicKey();
    }

    public long getUserId() {
        return userID;
    }
}
