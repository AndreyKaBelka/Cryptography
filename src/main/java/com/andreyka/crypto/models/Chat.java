package com.andreyka.crypto.models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Chat {
    private final long chatId;
    private final ArrayList<User> participantsList;

    public void addUser(final User user) {
        participantsList.add(user);
    }

    public void removeUser(final User user) {
        participantsList.remove(user);
    }

    public int getCountOfUsers() {
        return participantsList.size();
    }
}
