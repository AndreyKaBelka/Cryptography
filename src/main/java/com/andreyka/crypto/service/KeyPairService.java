package com.andreyka.crypto.service;

import com.andreyka.crypto.data.KeyPair;

public interface KeyPairService {
    KeyPair generateKeyPair() throws CloneNotSupportedException;
}
