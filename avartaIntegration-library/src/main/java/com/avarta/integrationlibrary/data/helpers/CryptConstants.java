package com.avarta.integrationlibrary.data.helpers;

class CryptConstants {

    static final int[] AES_MODE = new int[]{65, 69, 83, 47, 67, 66, 67, 47, 80, 75, 67, 83, 53, 80, 97, 100, 100, 105, 110, 103};
    static final int[] CHARSET = new int[]{85, 84, 70, 45, 56};
    static final int[] AES = new int[]{65, 69, 83};
    static final int[] PWD = new int[]{65, 116, 97, 100, 108, 119, 111, 110, 101, 99, 74, 101, 43, 84, 100, 116, 103, 122, 82, 47, 113, 103, 119, 100, 112, 43, 68, 100, 104, 76, 101, 75, 69, 71, 66, 55, 76, 81, 43, 110, 120, 43, 71, 56, 68, 104, 97, 104, 68, 102, 98, 51, 47, 52, 68, 115, 56, 99, 89, 77, 49, 55, 101, 50};
    static final int[] CIPHER = new int[]{98, 115, 99, 98, 85, 77, 117, 51, 114, 79, 55, 90, 43, 110, 88, 86, 68, 106, 79, 69, 113, 65, 61, 61};
    static final int[] VECTOR = new int[]{108, 103, 50, 48, 74, 78, 43, 89, 75, 74, 69, 78, 72, 43, 99, 52, 112, 68, 121, 97, 72, 98, 119, 79, 70, 113, 69, 78, 57, 118, 102, 47, 103, 79, 122, 120, 120, 103, 122, 88, 116, 55, 89, 61};

    protected static String get(int[] constantArray) {
        StringBuilder sb = new StringBuilder();
        for (int anIn : constantArray) {
            sb.append((char) anIn);
        }
        return sb.toString();
    }
}
