package com.andreyka.crypto.npone;

import com.andreyka.crypto.NPOneUtils;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.User;

public class ShareSecretsCommandForNew implements NPOneCommand {

    public void sharesGenerate(final Chat chat) {
        final User[] otherParticipants = NPOneUtils.getOtherParticipants(chat);
    }

    @Override
    public void execute() {

    }
}
