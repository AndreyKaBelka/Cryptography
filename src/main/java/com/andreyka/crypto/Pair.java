package com.andreyka.crypto;


public class Pair {
    public final String text;
    public final ECPoint signature;

    public Pair(String text, ECPoint signature) {
        this.text = text;
        this.signature = signature;
    }
}
