package com.andreyka.crypto.containers;

import com.andreyka.crypto.exceptions.NotFoundException;
import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.User;
import com.andreyka.crypto.utils.RandomUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public enum ChatsContainer {
    INSTANCE;

    private final List<Chat> container = new LinkedList<>();

    public void addUser(final long chatId, final User user) {
        getById(chatId).orElseThrow(
            () -> new NotFoundException("Chat with id %d not found.".formatted(chatId))
        ).addUser(user);
    }

    public long initChat() {
        long chatId = RandomUtils.generateLong();
        Chat chat = new Chat(chatId, new ArrayList<>());
        container.add(chat);
        return chatId;
    }

    public User getUserById(final long chatId, final long userId) {
        return getById(chatId).orElseThrow(
                () -> new NotFoundException("Chat with id %d not found.".formatted(chatId))
            ).getParticipantsList().stream()
            .filter((user) -> user.getUserId() == userId)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("User with id %d not found.".formatted(userId)));
    }

    public Optional<Chat> getById(final long chatId) {
        for (Chat chat : container) {
            if (chat.getChatId() == chatId) {
                return Optional.of(chat);
            }
        }
        return Optional.empty();
    }
}
