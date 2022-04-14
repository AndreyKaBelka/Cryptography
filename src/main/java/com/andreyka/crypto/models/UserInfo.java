package com.andreyka.crypto.models;

import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends User {
    long chatId;

    public UserInfo(long userId, PublicKey key, Long chatId) {
        super(userId, key);
        this.chatId = chatId;
    }
}
