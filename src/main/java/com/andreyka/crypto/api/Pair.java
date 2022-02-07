package com.andreyka.crypto.api;


import lombok.Value;

@Value
public class Pair {
    public String text;
    public Signature signature;
}
