package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.UserInfo;
import com.andreyka.crypto.models.keyexchange.GroupMessage;
import com.andreyka.crypto.utils.EncryptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class NewSessionInitTest {

    private long chatId;
    private UserInfo newUser;
    private Chat chat;

    @BeforeEach
    void setUp() {
        chatId = NponeTestUtils.initChat();
        chat = ChatsContainer.INSTANCE.getById(chatId).get();
        newUser = NponeTestUtils.createUser(chatId);
        new AuthenticationStepForOld().execute(newUser);
    }

    @Test
    void testSessionInit() {
        Map<Long, GroupMessage<String>> ans = new NewSessionInitCommand().execute(chat);
        Assertions.assertEquals(1, ans.keySet().size());
        Assertions.assertEquals(newUser.getUserId(), ans.keySet().toArray()[0]);
        GroupMessage<String> message = ans.get(newUser.getUserId());
        Assertions.assertDoesNotThrow(() -> EncryptionUtils.verifyGroupSignature(message));
    }

    @Test
    void testMoreUsers() {
        UserInfo newUser2 = NponeTestUtils.createUser(chatId);
        new AuthenticationStepForOld().execute(newUser2);

        Map<Long, GroupMessage<String>> ans = new NewSessionInitCommand().execute(chat);
        Assertions.assertEquals(2, ans.keySet().size());
        GroupMessage<String> message = ans.get(newUser2.getUserId());
        Assertions.assertDoesNotThrow(() -> EncryptionUtils.verifyGroupSignature(message));
    }
}
