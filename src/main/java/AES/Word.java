package AES;

public class Word {
    private int[] w;

    int getIndex(int index) {
        return w[index];
    }

    Word(int w1, int w2, int w3, int w4) {
        w = new int[4];
        w[0] = w1;
        w[1] = w2;
        w[2] = w3;
        w[3] = w4;
    }

    private Word() {
        w = new int[4];
    }

    static Word sum(Word word1, Word word2) {
        Word temp = new Word();

        for (int i = 0; i < 4; i++) {
            temp.w[i] = word1.w[i] ^ word2.w[i];
        }

        return temp;
    }

    static Word sum(Word word1, int val) {
        return sum(word1, new Word(val, 0, 0, 0));
    }

    static Word rotWord(Word word) {
        Word temp = new Word();
        int r = word.w[0];

        System.arraycopy(word.w, 1, temp.w, 0, 3);
        temp.w[3] = r;

        return temp;
    }

    static Word subWord(Word word) {
        Word temp = new Word();
        for (int i = 0; i < 4; i++) {
            temp.w[i] = Consts.subVal(word.w[i]);
        }
        return temp;
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
