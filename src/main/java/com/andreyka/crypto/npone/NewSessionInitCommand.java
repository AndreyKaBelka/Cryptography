package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.containers.SecretSharesContainer;
import com.andreyka.crypto.containers.SessionIdsContainer;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.utils.EncryptionUtils;
import com.andreyka.crypto.utils.NPOneUtils;

import java.math.BigInteger;
import java.util.Map;

public class NewSessionInitCommand implements NPOneCommand<Map<Long, GroupMessage<String>>, Chat> {
    @Override
    public Map<Long, GroupMessage<String>> execute(final Chat chat) {
        return newSessionGenerate(chat);
    }

    private Map<Long, GroupMessage<String>> newSessionGenerate(final Chat chat) {
        Hash sessionId = NPOneUtils.generateSessionId(chat);
        SessionIdsContainer.INSTANCE.addSessionIdForChat(chat.getChatId(), sessionId);

        long myUserId = MineInfoContainer.INSTANCE.getUserId(chat.getChatId());

        generateSecretShares(chat, sessionId, myUserId);
        Map<Long, GroupMessage<String>> groupMessages = encryptSecretShare(chat, myUserId);
        EncryptionUtils.groupSignature(chat.getChatId(), groupMessages);
        NPOneUtils.addKeyConfirmations(groupMessages);

        return groupMessages;
    }

    private Map<Long, GroupMessage<String>> encryptSecretShare(Chat chat, long myUserId) {
        User[] others = NPOneUtils.getOtherParticipants(chat);

        Hash mySecretShare = SecretSharesContainer.INSTANCE.getShareSecret(chat.getChatId(), myUserId);
        return EncryptionUtils.groupEncryption(mySecretShare, chat.getChatId(), others);
    }

    private void generateSecretShares(Chat chat, Hash sessionId, long myUserId) {
        Hash hash = new Hash();

        for (User user : NPOneUtils.getOtherParticipants(chat)) {
            long userId = user.getUserId();
            BigInteger commonKey = CommonKeysContainer.INSTANCE.getCommonKeyForUser(userId);
            hash = hash.xor(NPOneUtils.generateSecretShare(commonKey, sessionId));
        }
        SecretSharesContainer.INSTANCE.addSecretShareForChatIdAndUserId(
            chat.getChatId(),
            myUserId,
            hash
        );
    }
}
