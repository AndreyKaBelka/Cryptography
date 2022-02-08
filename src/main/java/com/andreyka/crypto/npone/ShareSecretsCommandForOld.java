package com.andreyka.crypto.npone;

import com.andreyka.crypto.NPOneUtils;
import com.andreyka.crypto.containers.CommonKeysContainer;
import com.andreyka.crypto.containers.KeyConfirmationContainer;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.User;

import java.math.BigInteger;

public class ShareSecretsCommandForOld implements NPOneCommand {
    @Override
    public void execute() {

    }

    public void sharesGenerate(final Chat chat) {
        final User lastParticipant = NPOneUtils.getNewUser(chat);

        BigInteger newKey = NPOneUtils.generateCommonKey(lastParticipant);
        CommonKeysContainer.INSTANCE.addCommonKeyForUser(lastParticipant.getUserId(), newKey);

        Hash keyConfirmation = NPOneUtils.getKeyConfirmationHash(newKey, lastParticipant.getUserId());
        KeyConfirmationContainer.INSTANCE.addKeyConfirmationForUser(lastParticipant.getUserId(), keyConfirmation);
    }

}
