package com.andreyka.crypto.npone;

import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.utils.NPOneUtils;

public class AuthenticationStepForOld implements NPOneCommand<Class<Void>, Chat> {
    @Override
    public Class<Void> execute(final Chat chat) {
        sharesGenerate(chat);
        return Void.TYPE;
    }

    public void sharesGenerate(final Chat chat) {
        final User lastParticipant = NPOneUtils.getNewUser(chat);

        NPOneUtils.generateKeyAndKeyConfirmation(chat.getChatId(), lastParticipant);
    }

}
