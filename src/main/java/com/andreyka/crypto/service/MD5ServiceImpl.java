package com.andreyka.crypto.service;

import org.springframework.stereotype.Service;

@Service
public class MD5ServiceImpl implements MD5Service {
    private final long powTo32 = 1L << 32;
    private final int[] SHIFT_AMTS = {
            7, 12, 17, 22, 5, 9, 14, 20, 4,
            11, 16, 23, 6, 10, 15, 21
    };
    private final int A = 0x67452301;
    private final int B = (int) 0xEFCDAB89L;
    private final int C = (int) 0x98BADCFEL;
    private final int D = 0x10325476;
    private final int[] T = new int[64];

    {
        for (int i = 0; i < 64; i++) {
            double sin = Math.abs(Math.sin(i + 1));
            T[i] = (int) (long) (powTo32 * sin);
        }
    }

    private int F(int X, int Y, int Z) {
        return (X & Y) | (~X & Z);
    }

    private int G(int X, int Y, int Z) {
        return (X & Z) | (~Z & Y);
    }

    private int H(int X, int Y, int Z) {
        return X ^ Y ^ Z;
    }

    private int I(int X, int Y, int Z) {
        return Y ^ (~Z | X);
    }

    @Override
    public String getHash(String string) {
        return getHash(string.getBytes());
    }

    private String getHash(byte[] bytes) {
        int A = this.A;
        int B = this.B;
        int C = this.C;
        int D = this.D;

        int messageLenBytes = bytes.length;
        int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
        int totalLen = numBlocks << 6;
        long messageLenBits = (long) messageLenBytes << 3;

        byte[] concatBytes = new byte[totalLen - messageLenBytes];
        concatBytes[0] = (byte) 0x80;

        for (int i = 0; i < 8; i++) {
            concatBytes[concatBytes.length - 8 + i] = (byte) messageLenBits;
            messageLenBits >>>= 8;
        }

        int[] state = new int[numBlocks << 4];
        for (int i = 0; i < numBlocks; i++) {
            int index = i << 6;
            for (int j = 0; j < 64; j++, index++) {
                state[j >>> 2] = ((int) ((index < messageLenBytes) ? bytes[index] : concatBytes[index - messageLenBytes]) << 24) | (state[j >>> 2] >>> 8);
            }

            int AA = A;
            int BB = B;
            int CC = C;
            int DD = D;

            for (int j = 0; j < 64; j++) {
                int div16 = j >>> 4;
                int f = 0;
                int bufferIndex = j;
                switch (div16) {
                    case 0:
                        f = F(B, C, D);
                        break;
                    case 1:
                        f = G(B, C, D);
                        bufferIndex = (bufferIndex * 5 + 1) & 0x0F;
                        break;
                    case 2:
                        f = H(B, C, D);
                        bufferIndex = (bufferIndex * 3 + 5) & 0x0F;
                        break;
                    case 3:
                        f = I(B, C, D);
                        bufferIndex = (bufferIndex * 7) & 0x0F;
                        break;
                }
                int temp = B + Integer.rotateLeft(A + f + state[bufferIndex] + T[j], SHIFT_AMTS[(div16 << 2) | (j & 3)]);
                A = D;
                D = C;
                C = B;
                B = temp;
            }
            A += AA;
            B += BB;
            C += CC;
            D += DD;
        }
        StringBuilder md5 = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int n = 0;
            switch (i) {
                case 0: {
                    n = A;
                    break;
                }
                case 1: {
                    n = B;
                    break;
                }
                case 2: {
                    n = C;
                    break;
                }
                case 3: {
                    n = D;
                    break;
                }
            }
            for (int j = 0; j < 4; j++) {
                md5.append(String.format("%02X", (byte) n));
                n >>>= 8;
            }
        }
        return md5.toString();
    }
}
