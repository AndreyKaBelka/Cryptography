package com.andreyka.crypto.encryption;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AESObject {
    private final byte[] text;
    private final BigInteger commonKey;
    private Word[] words;

    public static byte[] encrypt(String text, BigInteger commonKey) {
        AESObject aesObject = new AESObject(text.getBytes(StandardCharsets.UTF_8), commonKey);
        return aesObject.encrypt();
    }

    public static byte[] decrypt(byte[] text, BigInteger commonKey) {
        AESObject aesObject = new AESObject(text, commonKey);
        return aesObject.decrypt();
    }

    private byte[] encrypt() {
        this.words = keyExpansion(commonKey);
        return intToByteArray(encrypt(getArrayOfInt(text)));
    }

    private byte[] decrypt() {
        this.words = keyExpansion(commonKey);
        return intToByteArray(decrypt(getArrayOfInt(text)));
    }

    private int[] getArrayOfInt(byte[] bytes) {
        int[] arrangedBytes = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            arrangedBytes[i] = bytes[i] + 128;
        }
        return arrangedBytes;
    }

    private byte[] intToByteArray(int[] arr) {
        byte[] bytes = new byte[arr.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (arr[i] - 128);
        }
        return bytes;
    }

    private int[][] getState(int[] bytes) {
        int[][] state = new int[4][AESConsts.Nb];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < AESConsts.Nb; j++) {
                state[i][j] = bytes[i + 4 * j];
            }
        }
        return state;
    }

    private int[] getSubArr(int fromIndex, int toIndex, ArrayList<Integer> bytes) {
        int[] sub_arr = new int[4 * AESConsts.Nb];
        AtomicInteger inc = new AtomicInteger();
        bytes.subList(fromIndex, toIndex).forEach(val -> {
            sub_arr[inc.get()] = val;
            inc.getAndIncrement();
        });
        return sub_arr;
    }

    private int[] encrypt(int[] bytes_in) {
        ArrayList<Integer> bytes = new ArrayList<>();
        bytes.add(bytes_in.length);

        for (int value : bytes_in) {
            bytes.add(value);
        }

        if (bytes.size() % 16 != 0) {
            for (int i = 0; i < bytes.size() % 16; i++) {
                SecureRandom rnd = new SecureRandom();
                bytes.add(rnd.nextInt(256));
            }
        }

        int[] bytes_out = new int[4 * AESConsts.Nb * (bytes.size() >> 4)];

        for (int cnt = 0; cnt < bytes.size() >> 4; cnt++) {
            blockEncrypt(bytes, bytes_out, cnt);
        }

        return bytes_out;
    }

    private void blockEncrypt(ArrayList<Integer> bytes, int[] bytes_out, int cnt) {
        int[] sub_arr = getSubArr(cnt << 4, (cnt + 1) << 4, bytes);
        int[][] state = getState(sub_arr);

        addRoundKey(state, 0);

        for (int round = 1; round < AESConsts.Nr; round++) {
            oneRound(state, round);
        }

        finalRound(state);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < AESConsts.Nb; j++) {
                bytes_out[i + (cnt << 4) + (j << 2)] = state[i][j];
            }
        }
    }

    private void finalRound(int[][] state) {
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, AESConsts.Nr);
    }

    private void oneRound(int[][] state, int round) {
        subBytes(state);
        shiftRows(state);
        mixColumns(state);
        addRoundKey(state, round);
    }

    private int[] decrypt(int[] bytes_in) {
        ArrayList<Integer> bytes = (ArrayList<Integer>) Arrays.stream(bytes_in).boxed().collect(Collectors.toList());
        int[] bytes_out = new int[4 * AESConsts.Nb * (bytes.size() >> 4)];

        for (int cnt = 0; cnt < bytes.size() >> 4; cnt++) {
            blockDecrypt(bytes, bytes_out, cnt);
        }

        int[] normal = new int[bytes_out[0]];
        System.arraycopy(bytes_out, 1, normal, 0, normal.length);

        return normal;
    }

    private void blockDecrypt(ArrayList<Integer> bytes, int[] bytes_out, int cnt) {
        int[] sub_arr = getSubArr(cnt << 4, (cnt + 1) << 4, bytes);
        int[][] state = getState(sub_arr);

        addRoundKey(state, AESConsts.Nr);

        for (int round = AESConsts.Nr - 1; round >= 1; round--) {
            invRound(state, round);
        }
        invFinalRound(state);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < AESConsts.Nb; j++) {
                bytes_out[i + (j << 2) + (cnt << 4)] = state[i][j];
            }
        }
    }

    private void invFinalRound(int[][] state) {
        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, 0);
    }

    private void invRound(int[][] state, int round) {
        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, round);
        invMixColumns(state);
    }

    /**
     * The sum in field GF(256) is sum polynomials according to the following rule:
     * a = 131 = 10000011 = x^7 + x + 1
     * b = 95 = 01011111 = x^6 + x^4 + x^3 + x^2 + x + 1
     * a + b = x^7 + x^6 + x^4 + x^3 + x^2 OR a + b = a xor b
     *
     * @param a first polynomial of bytes
     * @param b second polynomial of butes
     * @return sum of polynomials
     */
    private int sum(int a, int b) {
        return a ^ b;
    }

    /**
     * The multiplication of polynomials in field GF(256) is multiplication polynomials according to the following rule:
     * a = 131 = 10000011 = x^7 + x + 1
     * b = 95 = 01011111 = x^6 + x^4 + x^3 + x^2 + x + 1
     * product = a * b mod (x^8 + x^4 + x^3 + x + 1)
     *
     * @param a1 first polynomial of bytes
     * @param b1 second polynomial of bytes
     * @return product of polynomials
     */
    private int mult(int a1, int b1) {
        int p = 0;
        int a = a1;
        int b = b1;
        while (a != 0 && b != 0) {
            if ((b & 0x01) == 1) p ^= a;

            if (a >= 128) a = (a << 1) ^ 0x11b;
            else a <<= 1;
            b >>= 1;
        }
        return p;
    }

    private Word[] keyExpansion(BigInteger key) {
        return keyExpansion(getArrayOfKeys(key));
    }

    /**
     * Function return array of rounded keys using algorithm of expanded key;
     *
     * @param key the input key
     * @return array of round keys
     */
    private Word[] keyExpansion(int[] key) {
        Word[] words = new Word[44];
        Word t;
        for (int i = 0; i < 4; i++) {
            words[i] = new Word(key[4 * i], key[4 * i + 1], key[4 * i + 2], key[4 * i + 3]);
        }
        for (int i = 4; i < words.length; i++) {
            if (i % 4 != 0) words[i] = Word.sum(words[i - 1], words[i - 4]);
            else {
                t = Word.sum(Word.subWord(Word.rotWord(words[i - 1])), AESConsts.rcon[i / 4]);
                words[i] = Word.sum(t, words[i - 4]);
            }
        }
        return words;
    }

    /**
     * Helper function for keyExpansion
     *
     * @param key the input key
     * @return array of bits key
     */
    private int[] getArrayOfKeys(BigInteger key) {
        int[] array_key = new int[16];
        BigInteger temp = key;
        for (int i = 0; i < 16; i++) {
            array_key[15 - i] = temp.mod(new BigInteger("256")).intValue();
            temp = temp.shiftRight(8);
        }
        return array_key;
    }

    /**
     * Function for replaced each byte Aij with a SubByte in s_box
     *
     * @param bytes array of bytes
     */
    private void subBytes(int[][] bytes) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < AESConsts.Nb; j++) {
                bytes[i][j] = AESConsts.subVal(bytes[i][j]);
            }
        }
    }

    private void shiftRows(int[][] bytes) {
        for (int i = 0; i < 4; i++) {
            nTimesShiftRow(bytes[i], i);
        }
    }

    private void nTimesShiftRow(int[] bytes, int times) {
        for (int i = 0; i < times; i++) {
            int temp = bytes[0];

            System.arraycopy(bytes, 1, bytes, 0, AESConsts.Nb - 1);
            bytes[AESConsts.Nb - 1] = temp;
        }
    }

    private void addRoundKey(int[][] bytes, int r) {
        for (int i = 0; i < AESConsts.Nb; i++) {
            for (int j = 0; j < 4; j++) {
                bytes[i][j] = sum(bytes[i][j], words[AESConsts.Nb * r + j].getValue(i));
            }
        }
    }

    private void mixColumns(int[][] bytes) {
        int[] col = new int[4];
        for (int i = 0; i < AESConsts.Nb; i++) {
            for (int j = 0; j < 4; j++) {
                col[j] = bytes[j][i];
            }
            bytes[0][i] = mult(0x02, col[0]) ^ mult(0x03, col[1]) ^ col[2] ^ col[3];
            bytes[1][i] = mult(0x02, col[1]) ^ mult(0x03, col[2]) ^ col[3] ^ col[0];
            bytes[2][i] = mult(0x02, col[2]) ^ mult(0x03, col[3]) ^ col[0] ^ col[1];
            bytes[3][i] = mult(0x02, col[3]) ^ mult(0x03, col[0]) ^ col[1] ^ col[2];
        }
    }

    private void invMixColumns(int[][] bytes) {
        int[] col = new int[4];
        for (int i = 0; i < AESConsts.Nb; i++) {
            for (int j = 0; j < 4; j++) {
                col[j] = bytes[j][i];
            }
            bytes[0][i] = mult(0x0e, col[0]) ^ mult(0x0b, col[1]) ^ mult(0x0d, col[2]) ^ mult(0x09, col[3]);
            bytes[1][i] = mult(0x09, col[0]) ^ mult(0x0e, col[1]) ^ mult(0x0b, col[2]) ^ mult(0x0d, col[3]);
            bytes[2][i] = mult(0x0d, col[0]) ^ mult(0x09, col[1]) ^ mult(0x0e, col[2]) ^ mult(0x0b, col[3]);
            bytes[3][i] = mult(0x0b, col[0]) ^ mult(0x0d, col[1]) ^ mult(0x09, col[2]) ^ mult(0x0e, col[3]);
        }
    }

    private void invShiftRows(int[][] bytes) {
        for (int i = 0; i < 4; i++) {
            invNTimesShiftRow(bytes[i], i);
        }
    }

    private void invNTimesShiftRow(int[] bytes, int times) {
        for (int i = 0; i < times; i++) {
            int temp = bytes[AESConsts.Nb - 1];

            System.arraycopy(bytes, 0, bytes, 1, AESConsts.Nb - 1);
            bytes[0] = temp;
        }
    }

    private void invSubBytes(int[][] bytes) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < AESConsts.Nb; j++) {
                bytes[i][j] = AESConsts.invSubVal(bytes[i][j]);
            }
        }
    }
}
