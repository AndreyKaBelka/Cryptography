package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.utils.NPOneUtils;

public class AuthenticationStepForNew implements NPOneCommand<Class<Void>, UserInfo> {

    @Override
    public Class<Void> execute(final UserInfo user) {
        sharesGenerate(user);
        return Void.TYPE;
    }

    public void sharesGenerate(final UserInfo user) {
        NPOneUtils.generateKeyAndKeyConfirmation(user.getChatId(), user);

        ChatsContainer.INSTANCE.addUser(user.getChatId(), getMyUser(user.getChatId()));
    }

    private User getMyUser(final long chatId) {
        MineInfoContainer.UserInfo userInfo = MineInfoContainer.INSTANCE.getUserInfo(chatId);
        return new User(userInfo.userId(), userInfo.keyPair().getPublicKey());
    }
}
