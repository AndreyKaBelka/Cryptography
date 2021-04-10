package com.andreyka.crypto;

import com.andreyka.crypto.Base64;
import org.junit.Assert;
import org.junit.Test;

public class Base64ServiceTest {
    @Test
    public void codingTest() {
        Assert.assertEquals(Base64.encode("quux"), "cXV1eA==");
        Assert.assertEquals(Base64.encode("foo"), "Zm9v");
        Assert.assertEquals(Base64.encode("fo"), "Zm8=");
        Assert.assertEquals(Base64.encode("f"), "Zg==");
        Assert.assertEquals(Base64.encode(""), "");
    }
}