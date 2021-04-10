package com.andreyka.crypto;

class Word {
    private final int[] w;

    Word(int w1, int w2, int w3, int w4) {
        w = new int[4];
        w[0] = w1;
        w[1] = w2;
        w[2] = w3;
        w[3] = w4;
    }

    Word() {
        w = new int[4];
    }

    static Word sum(Word word1, Word word2) {
        Word temp = new Word();

        for (int i = 0; i < 4; i++) {
            temp.setIndex(word1.getIndex(i) ^ word2.getIndex(i), i);
        }

        return temp;
    }

    static Word sum(Word word1, int val) {
        return sum(word1, new Word(val, 0, 0, 0));
    }

    static Word rotWord(Word word) {
        Word temp = new Word();
        int r = word.getIndex(0);

        arrayCopy(word, 1, temp, 0, 3);
        temp.setIndex(r, 3);

        return temp;
    }

    static Word subWord(Word word) {
        Word temp = new Word();
        for (int i = 0; i < 4; i++) {
            temp.setIndex(AESConsts.subVal(word.getIndex(i)), i);
        }
        return temp;
    }

    private static void arrayCopy(Word src, int srcPos, Word dest, int destPos, int length) {
        for (int i = destPos; i < destPos + length; i++) {
            dest.setIndex(src.getIndex(srcPos), i);
            srcPos++;
        }
    }

    int getIndex(int j) {
        return w[j];
    }

    void setIndex(int val, int index) {
        w[index] = val;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            temp.append(w[i]);
        }
        return temp.toString();
    }
}
