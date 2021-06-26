package com.andreyka.crypto;

import java.nio.ByteBuffer;

public class SHA2 {
    private static final int h0 = 0x6a09e667;
    private static final int h1 = 0xbb67ae85;
    private static final int h2 = 0x3c6ef372;
    private static final int h3 = 0xa54ff53a;
    private static final int h4 = 0x510e527f;
    private static final int h5 = 0x9b05688c;
    private static final int h6 = 0x1f83d9ab;
    private static final int h7 = 0x5be0cd19;

    private static final int[] K = new int[]{
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    public static String getHash(String string) {
        return getHash(string.getBytes());
    }

    static String getHash(byte[] bytes) {
        int h0 = SHA2.h0;
        int h1 = SHA2.h1;
        int h2 = SHA2.h2;
        int h3 = SHA2.h3;
        int h4 = SHA2.h4;
        int h5 = SHA2.h5;
        int h6 = SHA2.h6;
        int h7 = SHA2.h7;

        //Create array of bytes
        int length = (bytes.length + 1) << 3;
        int zerosCount = 448 - (length % 512);
        if (zerosCount < 0) {
            zerosCount += 512;
        }
        length = (length + zerosCount + 64) >> 3;
        byte[] bytes1 = new byte[length];

        //Pre-processing
        System.arraycopy(bytes, 0, bytes1, 0, bytes.length);
        bytes1[bytes.length] = (byte) 0b10000000;

        int cnt = 1;
        for (int i = (bytes1.length - 8); i < bytes1.length; i++) {
            byte b = getFirst8BitsWithOffset(((long) bytes.length << 3), (cnt++ * 8));
            bytes1[i] = b;
        }

        int numBlocks = length >>> 6;
        for (int blockNum = 0; blockNum < numBlocks; blockNum++) {
            byte[] block = new byte[64];
            System.arraycopy(bytes1, 64 * blockNum, block, 0, 64);

            //get words
            int[] words = new int[64];
            for (int i = 0; i < 16; i++) {
                byte[] wordBytes = new byte[4];
                System.arraycopy(block, 4 * i, wordBytes, 0, 4);
                words[i] = ByteBuffer.wrap(wordBytes).getInt();
            }

            for (int i = 16; i < 64; i++) {
                int s0 = rightRotate(words[i - 15], 7) ^ rightRotate(words[i - 15], 18) ^ (words[i - 15] >>> 3);
                int s1 = rightRotate(words[i - 2], 17) ^ rightRotate(words[i - 2], 19) ^ (words[i - 2] >>> 10);
                words[i] = words[i - 16] + s0 + words[i - 7] + s1;
            }

            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = h5;
            int g = h6;
            int h = h7;

            for (int i = 0; i < words.length; ++i) {
                int s1 = rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25);
                int ch = (e & f) ^ (~e & g);
                int temp1 = h + s1 + ch + K[i] + words[i];
                int s0 = rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = s0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }

            h0 = h0 + a;
            h1 = h1 + b;
            h2 = h2 + c;
            h3 = h3 + d;
            h4 = h4 + e;
            h5 = h5 + f;
            h6 = h6 + g;
            h7 = h7 + h;
        }


        return toHex(h0) +
                toHex(h1) +
                toHex(h2) +
                toHex(h3) +
                toHex(h4) +
                toHex(h5) +
                toHex(h6) +
                toHex(h7);
    }

    private static byte getFirst8BitsWithOffset(long val, int offset) {
        return (byte) ((val >>> (64 - offset)) & 0b11111111);
    }

    private static int rightRotate(int val, int n) {
        return (val >>> n) | (val << -n);
    }

    private static String toHex(int val) {
        return String.format("%08x", val);
    }
}
