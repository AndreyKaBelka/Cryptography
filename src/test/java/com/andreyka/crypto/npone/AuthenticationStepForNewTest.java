package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationStepForNewTest {
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        long chatId = ChatsContainer.INSTANCE.initChat();
        MineInfoContainer.UserInfo user = MineInfoContainer.INSTANCE.getUserInfo(chatId);
        userInfo = new UserInfo(user.userId(), user.keyPair().getPublicKey(), chatId);
    }

    @Test
    void addNewUser() {
        new AuthenticationStepForNew().execute(userInfo);

        final User user = ChatsContainer.INSTANCE.getUserById(userInfo.getChatId(), userInfo.getUserId());

        Assertions.assertEquals(user.getUserId(), userInfo.getUserId());
        Assertions.assertEquals(user.getKey(), userInfo.getKey());
    }
}
