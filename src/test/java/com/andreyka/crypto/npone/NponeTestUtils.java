package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.KeyPair;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.utils.RandomUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NponeTestUtils {

    public long initChat() {
        long chatId = ChatsContainer.INSTANCE.initChat();
        MineInfoContainer.UserInfo myUser = MineInfoContainer.INSTANCE.getUserInfo(chatId);
        new AuthenticationStepForNew().execute(new UserInfo(myUser.userId(), myUser.keyPair().getPublicKey(), chatId));

        return chatId;
    }

    public UserInfo createUser(final long chatId) {
        return new UserInfo(RandomUtils.generateLong(), new KeyPair().getPublicKey(), chatId);
    }

    public UserInfo createUser() {
        return new UserInfo(RandomUtils.generateLong(), new KeyPair().getPublicKey(), RandomUtils.generateLong());
    }

    public UserInfo getMyUser(long chatId) {
        MineInfoContainer.UserInfo myUser = MineInfoContainer.INSTANCE.getUserInfo(chatId);
        return new UserInfo(myUser.userId(), myUser.keyPair().getPublicKey(), chatId);
    }
}
