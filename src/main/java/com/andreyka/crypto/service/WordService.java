package com.andreyka.crypto.service;

import com.andreyka.crypto.data.Word;

public interface WordService {
    int getWordIndex(Word word, int index);

    Word sum(Word word1, Word word2);

    Word sum(Word word1, int val);

    Word rotWord(Word word);

    Word subWord(Word word);
}
