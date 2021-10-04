package com.andreyka.crypto.hashes;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class SHA2Test extends TestCase {

    @Test
    public void testShortVectors() throws IOException {
        URL path = getClass().getClassLoader().getResource("SHA256ShortMsg.rsp");
        testVectors(path);
    }

    @Test
    public void testLongVectors() throws IOException {
        URL path = getClass().getClassLoader().getResource("SHA256LongMsg.rsp");
        testVectors(path);
    }

    private void testVectors(URL path) throws IOException {
        if (path == null) {
            Assert.fail();
        }
        try (BufferedReader reader = Files.newBufferedReader(Path.of(path.toURI()))) {
            ArrayList<String> strings = new ArrayList<>();

            for (String line; (line = reader.readLine()) != null; ) {
                if (!line.equals("")) {
                    strings.add(line);
                } else {
                    byte[] msg = hexToBin(strings.get(1).split(" ")[2]);
                    String actualHash = SHA2.getHash(msg);
                    Assert.assertEquals(strings.get(2).split(" ")[2], actualHash);
                    strings.clear();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private byte[] hexToBin(String str) {
        if (str.equals("00")) return new byte[0];
        int len = str.length();
        byte[] out = new byte[len / 2];
        int endIndx;

        for (int i = 0; i < len; i = i + 2) {
            endIndx = i + 2;
            if (endIndx > len)
                endIndx = len - 1;
            out[i / 2] = (byte) Integer.parseInt(str.substring(i, endIndx), 16);
        }
        return out;
    }
}