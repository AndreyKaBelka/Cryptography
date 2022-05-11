package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationStepForNewTest {
    private long chatId;

    @BeforeEach
    void setUp() {
        chatId = RandomUtils.generateLong();
        ChatsContainer.INSTANCE.initChat(chatId);
        ChatsContainer.INSTANCE.addUser(chatId, NponeTestUtils.getMyUser(chatId));
    }

    @Test
    void addNewUser() {
        UserInfo oldUser = NponeTestUtils.createUser(chatId);

        new AuthenticationStepForNew().execute(oldUser);

        final User user = ChatsContainer.INSTANCE.getUserById(oldUser.getChatId(), oldUser.getUserId());

        Assertions.assertEquals(oldUser, user);
        int countOfUsers = ChatsContainer.INSTANCE.getById(chatId).get().getCountOfUsers();
        Assertions.assertEquals(2, countOfUsers);
    }
}
