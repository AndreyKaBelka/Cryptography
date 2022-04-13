package com.andreyka.crypto.encryption;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@UtilityClass
public class Base64 {
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static String encode(String text) {
        if (text.isBlank()) {
            return "";
        }
        return encode(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        StringBuilder rez = new StringBuilder();
        int cnt_zer_bytes = 3 - bytes.length % 3;
        byte[] copy;

        copy = new byte[bytes.length + cnt_zer_bytes % 3];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);

        for (int i = 0; i < (cnt_zer_bytes % 3); i++) {
            copy[copy.length - 1 - i] = 0;
        }

        for (int i = 0; i < copy.length; i += 3) {
            byte lastBits = 0;
            byte highest_bits;
            for (int j = 0; j < 3; j++) {
                byte cnt_bits = (byte) ((byte) (j + 1) << 1);
                byte mask = (byte) ((1 << cnt_bits) - 1);
                byte mask_127 = (byte) (127 >> (cnt_bits - 1));
                highest_bits = (byte) (copy[i + j] >> cnt_bits);
                highest_bits &= mask_127;

                byte index = (byte) (highest_bits & 0xff | (lastBits << (8 - cnt_bits)));
                rez.append(chars.charAt(index));

                lastBits = (byte) (copy[i + j] & mask);
            }
            rez.append(chars.charAt(lastBits));
        }

        if (cnt_zer_bytes % 3 > 0) {
            rez.replace(rez.length() - cnt_zer_bytes, rez.length(), "=".repeat(cnt_zer_bytes));
        }

        return rez.toString();
    }

    public static byte[] decode(String text) {
        if (text.isBlank()) {
            return "".getBytes();
        }
        return decode(text.getBytes());
    }

    public static byte[] decode(byte[] bytes) {
        ArrayList<Byte> bytes_out = new ArrayList<>();

        byte[] bytes1 = new byte[bytes.length];

        for (int i = 0; i < bytes1.length; i++) {
            bytes1[i] = (byte) chars.indexOf(bytes[i]);
        }

        for (int i = 0; i < bytes1.length; i += 4) {
            for (int j = 0; j < 3; j++) {
                byte next_val = bytes1[i + j + 1];
                if (next_val == -1) {
                    break;
                }

                byte cnt_bits = (byte) (4 - (j << 1));
                int lastBits = next_val >> cnt_bits;

                byte shifting_mask = (byte) (j << 1);
                byte mask = (byte) (63 >> shifting_mask);

                byte last_digit_val = (byte) (bytes1[i + j] & mask);

                byte ch = (byte) ((last_digit_val << (6 - cnt_bits)) | (lastBits));
                bytes_out.add(ch);
            }
        }

        return listToArr(bytes_out);
    }

    private static byte[] listToArr(ArrayList<Byte> arr) {
        byte[] out = new byte[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            out[i] = arr.get(i);
        }

        return out;
    }

}
