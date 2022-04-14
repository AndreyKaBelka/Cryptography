package com.andreyka.crypto.utils;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;

@UtilityClass
public class ByteUtils {
    private static final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }
}
