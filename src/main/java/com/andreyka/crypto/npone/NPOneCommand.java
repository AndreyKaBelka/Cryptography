package com.andreyka.crypto.npone;

import com.andreyka.crypto.models.Chat;
import com.andreyka.crypto.models.keyexchange.GroupMessage;

import java.util.Map;

public interface NPOneCommand<T, V> {
    T execute(V val);
}
