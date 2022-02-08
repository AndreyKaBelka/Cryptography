package com.andreyka.crypto;

import com.andreyka.crypto.containers.KeyConfirmationContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.hashes.SHA2;
import com.andreyka.crypto.models.*;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.models.keyexchange.KeyConfirmation;
import com.andreyka.crypto.models.keyexchange.SecretShare;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

@UtilityClass
public class NPOneUtils {

    public BigInteger generateCommonKey(final User user) {
        PrivateKey key = MineInfoContainer.INSTANCE.getPrivateKey();
        return user.getKey().getCommonKey(key);
    }

    public <T> void addKeyEncryption(Map<Long, GroupMessage<String>> messages) {
        for (Map.Entry<Long, GroupMessage<String>> pair: messages.entrySet()) {
            Hash keyConfirmation = KeyConfirmationContainer.INSTANCE.getKeyConfirmationForUser(pair.getKey());
            pair.getValue().setKeyConfirmation(keyConfirmation);
        }
    }

    public User[] getOtherParticipants(final Chat chat) {
        long myId = MineInfoContainer.INSTANCE.getUserId();
        return chat.getParticipantsList().stream().filter((user) -> user.getUserId() != myId).toArray(User[]::new);
    }

    public User getNewUser(Chat chat) {
        int lastIndex = chat.getParticipantsList().size();
        return chat.getParticipantsList().get(lastIndex);
    }

    public Hash getKeyConfirmationHash(final BigInteger commonKey, final long userId) {
        return KeyConfirmation.create(commonKey, userId).hash();
    }

    public Hash generateSessionId(final Chat chat) {
        ArrayList<User> participants = chat.getParticipantsList();
        long sessionId = participants.get(0).bitSum();
        for (int i = 1; i < participants.size(); i++) {
            sessionId |= participants.get(i).bitSum();
        }

        return SHA2.getHash(sessionId);
    }

    public Hash generateSecretShare(final BigInteger commonKey, final Hash sessionId) {
        return SecretShare.create(commonKey, sessionId).generate();
    }
}
