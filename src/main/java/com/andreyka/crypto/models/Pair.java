package com.andreyka.crypto.models;


import lombok.Value;

@Value
public class Pair {
    public String text;
    public Signature signature;
}
