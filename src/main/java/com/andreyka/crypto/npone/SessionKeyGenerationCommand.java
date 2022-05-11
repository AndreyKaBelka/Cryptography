package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.KeyConfirmationContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.containers.SecretSharesContainer;
import com.andreyka.crypto.exceptions.CryptoOperationException;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.utils.EncryptionUtils;
import com.andreyka.crypto.utils.NPOneUtils;

import java.math.BigInteger;

public class SessionKeyGenerationCommand implements NPOneCommand<GroupMessage<String>, GroupMessage<String>> {
    @Override
    public GroupMessage<String> execute(GroupMessage<String> message) {
        return generateSessionKey(message);
    }

    private GroupMessage<String> generateSessionKey(final GroupMessage<String> message) {
        Hash keyConfirmation = KeyConfirmationContainer.INSTANCE.getKeyConfirmationForUser(message.getUserId());

        if (!keyConfirmation.equals(message.getKeyConfirmation())) {
            throw new CryptoOperationException("Key`s confirmation don't equals!");
        }

        EncryptionUtils.verifyGroupSignature(message);
        String secretShare = decrypt(message);

        SecretSharesContainer.INSTANCE.addSecretShareForChatIdAndUserId(
            message.getChatId(),
            message.getUserId(),
            new Hash(secretShare)
        );

        return encryptSessionKey(message.getChatId());
    }

    private String decrypt(GroupMessage<String> message) {
        BigInteger commonKey = CommonKeysContainer.INSTANCE.getCommonKeyForUser(message.getUserId());

        return EncryptionUtils.decrypt(message.getMessage(), commonKey);
    }

    private GroupMessage<String> encryptSessionKey(long chatId) {
        Hash sessionKey = NPOneUtils.generateSessionKey(chatId);
        Hash message = sessionKey.xor(MineInfoContainer.INSTANCE.getUserId(chatId));
        GroupMessage<String> sessionConfirmation = new GroupMessage<>(message.toString(), MineInfoContainer.INSTANCE.getUserId(chatId), chatId);
        sessionConfirmation.setSignature(EncryptionUtils.signature(sessionKey, MineInfoContainer.INSTANCE.getPrivateKey(chatId)));
        return sessionConfirmation;
    }
}
