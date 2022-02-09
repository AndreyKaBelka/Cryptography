package com.andreyka.crypto.npone;

import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.utils.NPOneUtils;

public class AuthenticationStepForNew implements NPOneCommand<Class<Void>> {

    @Override
    public Class<Void> execute(final Chat chat) {
        sharesGenerate(chat);
        return Void.class;
    }

    public void sharesGenerate(final Chat chat) {
        final User[] otherParticipants = NPOneUtils.getOtherParticipants(chat);

        for (User user : otherParticipants) {
            NPOneUtils.generateKeyAndKeyConfirmation(user);
        }
    }
}
