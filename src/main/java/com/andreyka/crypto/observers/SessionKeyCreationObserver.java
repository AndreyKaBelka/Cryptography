package com.andreyka.crypto.observers;

import com.andreyka.crypto.containers.*;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.Pair;
import com.andreyka.crypto.models.PrivateKey;
import com.andreyka.crypto.models.Signature;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.utils.EncryptionUtils;

import java.util.List;

public class SessionKeyCreationObserver implements IObserver {
    @Override
    public void notify(final long chatId) {
        int countOfUsers = ChatsContainer.INSTANCE.getById(chatId).orElseThrow().getCountOfUsers();
        if (countOfUsers == SecretSharesContainer.INSTANCE.count(chatId)) {
            Hash sessionKey = generateSessionKey(chatId);
            GroupMessage<String> sessionKeyMessage = genSessionConfirmation(sessionKey, chatId);
//          TODO: broadcast(sessionKeyMessage);
        }
    }

    private Hash generateSessionKey(final long chatId) {
        Hash res = new Hash();
        List<Hash> secretShares = SecretSharesContainer.INSTANCE.getSecretShares(chatId);
        Hash sessionId = SessionIdsContainer.INSTANCE.getSessionIdByChatId(chatId);
        for (Hash hash : secretShares) {
            res.xor(hash);
        }
        res.xor(sessionId);
        SessionKeysContainer.INSTANCE.addSessionKey(chatId, res);
        return res;
    }

    private GroupMessage<String> genSessionConfirmation(final Hash sessionKey, final long chatId) {
        long myUserId = MineInfoContainer.INSTANCE.getUserId(chatId);
        Hash sessionConfirmation = SHA2.getHash(sessionKey.getStringHash() + myUserId);
        PrivateKey privateKey = MineInfoContainer.INSTANCE.getPrivateKey(chatId);
        Signature signature = EncryptionUtils.signature(sessionConfirmation, privateKey);
        Pair message = new Pair(sessionConfirmation.toString(), signature);

        return new GroupMessage<>(message.toString(), myUserId, chatId);
    }
}
