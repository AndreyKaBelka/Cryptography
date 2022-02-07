package com.andreyka.crypto.encryption;

import lombok.ToString;

@ToString
class Word {
    private final int[] w;

    Word(int w1, int w2, int w3, int w4) {
        w = new int[]{w1, w2, w3, w4};
    }

    Word() {
        w = new int[4];
    }

    static Word sum(Word word1, Word word2) {
        Word temp = new Word();

        for (int i = 0; i < 4; i++) {
            temp.setValue(word1.getValue(i) ^ word2.getValue(i), i);
        }

        return temp;
    }

    static Word sum(Word word1, int val) {
        return sum(word1, new Word(val, 0, 0, 0));
    }

    static Word rotWord(Word word) {
        Word temp = new Word();
        int r = word.getValue(0);

        arrayCopy(word, 1, temp, 0, 3);
        temp.setValue(r, 3);

        return temp;
    }

    static Word subWord(Word word) {
        Word temp = new Word();
        for (int i = 0; i < 4; i++) {
            temp.setValue(AESConsts.subVal(word.getValue(i)), i);
        }
        return temp;
    }

    private static void arrayCopy(Word src, int srcPos, Word dest, int destPos, int length) {
        for (int i = destPos; i < destPos + length; i++) {
            dest.setValue(src.getValue(srcPos), i);
            srcPos++;
        }
    }

    int getValue(int j) {
        return w[j];
    }

    void setValue(int val, int index) {
        w[index] = val;
    }
}
