package com.andreyka.crypto.service;

import com.andreyka.crypto.data.AESConsts;
import com.andreyka.crypto.data.Word;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {

    @Override
    public int getWordIndex(Word word, int index) {
        return word.getIndex(index);
    }

    @Override
    public Word sum(Word word1, Word word2) {
        Word temp = new Word();

        for (int i = 0; i < 4; i++) {
            temp.setIndex(word1.getIndex(i) ^ word2.getIndex(i), i);
        }

        return temp;
    }

    @Override
    public Word sum(Word word1, int val) {
        return sum(word1, new Word(val, 0, 0, 0));
    }

    @Override
    public Word rotWord(Word word) {
        Word temp = new Word();
        int r = word.getIndex(0);

        arrayCopy(word, 1, temp, 0, 3);
        temp.setIndex(r, 3);

        return temp;
    }

    @Override
    public Word subWord(Word word) {
        Word temp = new Word();
        for (int i = 0; i < 4; i++) {
            temp.setIndex(AESConsts.subVal(word.getIndex(i)), i);
        }
        return temp;
    }

    private void arrayCopy(Word src, int srcPos, Word dest, int destPos, int length) {
        for (int i = destPos; i < destPos + length; i++) {
            dest.setIndex(src.getIndex(srcPos), i);
            srcPos++;
        }
    }
}
