package com.andreyka.crypto.models.keyexchange;

import com.andreyka.crypto.containers.SessionIdsContainer;
import com.andreyka.crypto.models.Hash;
import com.andreyka.crypto.models.Signature;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
public class GroupMessage<T> {
    T message;
    long userId;

    Long chatId;

    @NonFinal
    @Setter
    Signature signature;

    @NonFinal
    @Setter
    Hash keyConfirmation;

    public String forSign() {
        Hash sessionId = SessionIdsContainer.INSTANCE.getSessionIdByChatId(chatId);

        return userId + message.toString() + sessionId.toString();
    }
}
