package com.andreyka.crypto.npone;

import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.keyexchange.GroupMessage;

import java.util.Map;

public interface NPOneCommand<T> {
    T execute(Chat chat);
}
