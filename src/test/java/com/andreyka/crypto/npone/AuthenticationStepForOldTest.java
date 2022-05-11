package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.KeyConfirmationContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.KeyPair;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

class AuthenticationStepForOldTest {
    private UserInfo newUser;

    @BeforeEach
    void setUp() {
        long chatId = NponeTestUtils.initChat();
        newUser = NponeTestUtils.createUser(chatId);
    }

    @Test
    void execute() {
        new AuthenticationStepForOld().execute(newUser);
        Chat byId = ChatsContainer.INSTANCE.getById(newUser.getChatId()).get();
        Hash keyConfirmationForUser = KeyConfirmationContainer.INSTANCE.getKeyConfirmationForUser(newUser.getUserId());

        // Проверка, что пользователь был добавлен
        Assertions.assertEquals(2, byId.getCountOfUsers());
        //Проверка, что пользователь был добавлен верно
        Assertions.assertEquals(byId.getParticipantsList().get(1), newUser);
        //Проверка, что подтверждение ключа было создано верно
        Assertions.assertNotEquals(keyConfirmationForUser, new Hash());
    }

    @Test
    void executeForLargeAmountOfUser() {
        final long chatId = NponeTestUtils.initChat();
        final int countOfNewUsers = 100;
        final Hash[] keyConfirmations = new Hash[countOfNewUsers];

        for(int i = 0; i < countOfNewUsers; i++) {
            UserInfo newUser = NponeTestUtils.createUser(chatId);
            new AuthenticationStepForOld().execute(newUser);
            keyConfirmations[i] = KeyConfirmationContainer.INSTANCE.getKeyConfirmationForUser(newUser.getUserId());
        }
        Chat byId = ChatsContainer.INSTANCE.getById(chatId).get();

        Assertions.assertFalse(Arrays.stream(keyConfirmations).anyMatch(hash -> hash.equals(new Hash())));
        Assertions.assertEquals(countOfNewUsers + 1, byId.getCountOfUsers());
    }
}