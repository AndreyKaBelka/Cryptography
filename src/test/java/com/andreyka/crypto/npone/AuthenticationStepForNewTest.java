package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationStepForNewTest {
    private Chat chat;

    @BeforeEach
    void setUp() {
        long chatId = ChatsContainer.INSTANCE.initChat();
        chat = ChatsContainer.INSTANCE.getById(chatId).get();
    }

    @Test
    void addNewUser() {
        new AuthenticationStepForNew().execute(chat);

        Assertions.assertEquals(
            MineInfoContainer.INSTANCE.getUserId(chat.getChatId()),
            chat.getParticipantsList().get(0).getUserId()
        );

        Assertions.assertEquals(
            MineInfoContainer.INSTANCE.getPublicKey(chat.getChatId()),
            chat.getParticipantsList().get(0).getKey()
        );
    }
}
