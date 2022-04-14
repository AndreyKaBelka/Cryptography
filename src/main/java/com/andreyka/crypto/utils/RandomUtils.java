package com.andreyka.crypto.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class RandomUtils {
    @SneakyThrows
    public long generateLong() {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        return secureRandom.nextLong();
    }
}
