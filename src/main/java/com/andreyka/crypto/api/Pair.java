package com.andreyka.crypto.api;


public class Pair {
    public final String text;
    public final ECPoint signature;

    public Pair(String text, ECPoint signature) {
        this.text = text;
        this.signature = signature;
    }
}
