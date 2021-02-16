package com.andreyka.crypto.service;

public interface Base64Service {
    String encode(String text);

    String encode(byte[] bytes);

    byte[] decode(String text);
}
