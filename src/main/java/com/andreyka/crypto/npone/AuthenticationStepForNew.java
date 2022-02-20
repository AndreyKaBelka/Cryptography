package com.andreyka.crypto.npone;

import com.andreyka.crypto.containers.ChatsContainer;
import com.andreyka.crypto.containers.MineInfoContainer;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.PublicKey;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.utils.NPOneUtils;

public class AuthenticationStepForNew implements NPOneCommand<Class<Void>, Chat> {

    @Override
    public Class<Void> execute(final Chat chat) {
        sharesGenerate(chat);
        return Void.TYPE;
    }

    public void sharesGenerate(final Chat chat) {
        final User[] otherParticipants = NPOneUtils.getOtherParticipants(chat);

        for (User user : otherParticipants) {
            NPOneUtils.generateKeyAndKeyConfirmation(chat.getChatId(), user);
        }

        ChatsContainer.INSTANCE.addUser(chat.getChatId(), getMyUser(chat.getChatId()));
    }

    private User getMyUser(final long chatId) {
        long userId = MineInfoContainer.INSTANCE.getUserId(chatId);
        PublicKey publicKey = MineInfoContainer.INSTANCE.getPublicKey(chatId);
        return new User(userId, publicKey);
    }
}
