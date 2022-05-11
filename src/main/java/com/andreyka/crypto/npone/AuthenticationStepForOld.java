package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.utils.NPOneUtils;

public class AuthenticationStepForOld implements NPOneCommand<Class<Void>, UserInfo> {
    @Override
    public Class<Void> execute(final UserInfo user) {
        ChatsContainer.INSTANCE.addUser(user.getChatId(), user);
        sharesGenerate(user);
        return Void.TYPE;
    }

    public void sharesGenerate(final UserInfo user) {
        NPOneUtils.generateKeyAndKeyConfirmation(user.getChatId(), user);
    }

}