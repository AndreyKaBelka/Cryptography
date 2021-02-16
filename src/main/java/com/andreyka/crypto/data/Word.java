package com.andreyka.crypto.data;

public class Word {
    private int[] w;

    public Word(int w1, int w2, int w3, int w4) {
        w = new int[4];
        w[0] = w1;
        w[1] = w2;
        w[2] = w3;
        w[3] = w4;
    }

    public Word() {
        w = new int[4];
    }

    public int getIndex(int j) {
        return w[j];
    }

    public void setIndex(int val, int index) {
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
