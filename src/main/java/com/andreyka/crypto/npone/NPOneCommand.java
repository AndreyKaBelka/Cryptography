package com.andreyka.crypto.npone;

public interface NPOneCommand<T, V> {
    T execute(V val);
}
