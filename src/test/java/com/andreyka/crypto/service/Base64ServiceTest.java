package com.andreyka.crypto.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Base64ServiceTest {
    private Base64Service base64Service;

    @Before
    public void setUp() {
        base64Service = new Base64ServiceImpl();
    }

    @Test
    public void codingTest() {
        Assert.assertEquals(base64Service.encode("quux"), "cXV1eA==");
        Assert.assertEquals(base64Service.encode("foo"), "Zm9v");
        Assert.assertEquals(base64Service.encode("fo"), "Zm8=");
        Assert.assertEquals(base64Service.encode("f"), "Zg==");
        Assert.assertEquals(base64Service.encode(""), "");
    }
}