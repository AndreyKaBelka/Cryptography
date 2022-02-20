package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.SessionKeysContainer;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.PublicKey;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.utils.EncryptionUtils;

public class VerifySessionKeyCommand implements NPOneCommand<Class<Void>, GroupMessage<String>> {
    @Override
    public Class<Void> execute(GroupMessage<String> val) {
        checkSessionKey(val);
        return Void.TYPE;
    }

    private void checkSessionKey(GroupMessage<String> sessionKeyMessage) {
        Long chatId = sessionKeyMessage.getChatId();
        String sessionKey = SessionKeysContainer.INSTANCE.getSessionKey(chatId).getStringHash();
        Hash sessionConfirmation = SHA2.getHash(sessionKey + sessionKeyMessage.getUserId());

        if (sessionConfirmation.getStringHash().equals(sessionKeyMessage.getMessage())) {
            PublicKey publicKey = ChatsContainer.INSTANCE.getUserById(chatId, sessionKeyMessage.getUserId()).getKey();
            EncryptionUtils.validateSign(sessionConfirmation, publicKey, sessionKeyMessage.getSignature());
        }
    }
}
