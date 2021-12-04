package com.avarta.integrationlibrary.data.api;

class EncryptionConstants {

    public static String getString(int[] constantArray) {
        StringBuilder sb = new StringBuilder();
        for (int anIn : constantArray) {
            sb.append((char) anIn);
        }
        return sb.toString();
    }

    static int getInt(int[] constantArray) {
        StringBuilder sb = new StringBuilder();
        for (int anIn : constantArray) {
            sb.append((char) anIn);
        }
        return Integer.parseInt(sb.toString());
    }

    static final int[] GLOBAL_REQUEST_TAG = new int[]{83, 79, 76, 85, 83, 95, 67, 79, 78, 78, 69, 67, 84};

    public static final int[] API = new int[]{97, 112, 105, 47, 118, 49, 47};

    static final int[] URI_GET_STATUS = new int[]{119, 111, 114, 107, 102, 108, 111, 119, 47, 115, 116, 97, 116, 117, 115, 116, 121, 112, 101};
    static final int[] URI_PROCESS = new int[]{119, 111, 114, 107, 102, 108, 111, 119, 47, 112, 114, 111, 99, 101, 115, 115, 105, 116, 101, 109};
    static final int[] URI_START_WORKFLOW = new int[]{119, 111, 114, 107, 102, 108, 111, 119, 47, 115, 116, 97, 114, 116};
    static final int[] URI_COMPLETE = new int[]{119, 111, 114, 107, 102, 108, 111, 119, 47, 99, 111, 109, 112, 108, 101, 116, 101};
    static final int[] URI_CONFIGURATION = new int[]{111, 114, 103, 97, 110, 105, 115, 97, 116, 105, 111, 110, 47, 97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 99, 111, 110, 102, 105, 103, 117, 114, 97, 116, 105, 111, 110};
    static final int[] URI_SESSION = new int[]{115, 101, 115, 115, 105, 111, 110, 47, 99, 104, 101, 99, 107};
    static final int[] URI_ABORT = new int[]{119, 111, 114, 107, 102, 108, 111, 119, 47, 97, 98, 111, 114, 116};
    static final int[] URI_CHECK_APP_VERSION = new int[]{100, 105, 97, 103, 110, 111, 115, 116, 105, 99, 115, 47, 83, 117, 112, 112, 111, 114, 116, 101, 100, 67, 108, 105, 101, 110, 116, 86, 101, 114, 115, 105, 111, 110};
    static final int[] URI_BEHAVIOSEC = new int[]{97, 112, 105, 47, 118, 49, 47, 98, 101, 104, 97, 118, 105, 111, 117, 114, 47, 108, 111, 103, 98, 101, 104, 97, 118, 105, 111, 117, 114};
    static final int[] URI_CALCULATE_USER_SCORES = new int[]{97, 112, 105, 47, 118, 49, 47, 117, 115, 101, 114, 115, 99, 111, 114, 101, 47, 99, 97, 108, 99, 117, 108, 97, 116, 101, 117, 115, 101, 114, 115, 99, 111, 114, 101, 115};
    static final int[] URI_CHANGE_PASSWORD = new int[]{117, 115, 101, 114, 117, 112, 100, 97, 116, 101, 47, 99, 104, 97, 110, 103, 101, 117, 115, 101, 114, 112, 97, 115, 115, 119, 111, 114, 100, 10};

    static final int[] UNKNOWN_SUCCESS_CODE = new int[]{45, 50, 48, 48};
    static final int[] UNKNOWN_ERROR_CODE = new int[]{45, 53, 48, 48};
    static final int[] ON_TIMEOUT_CODE = new int[]{45, 53, 48, 49};

    static final int[] REQUEST_TIMEOUT_MS = new int[]{51, 48, 48, 48, 48};

    static final int[] AUTH_USER_DEF = new int[]{115, 111, 108, 117, 115, 95, 97, 112, 105, 95, 117, 115, 101, 114};
    static final int[] AUTH_PASSWD_DEF = new int[]{115, 111, 108, 117, 115, 95, 97, 112, 105, 95, 112, 97, 115, 115, 119, 111, 114, 100};

    static final int[] SSL_ERROR = new int[]{83, 111, 114, 114, 121, 44, 32, 115, 101, 99, 117, 114, 101, 32, 99, 111, 110, 110, 101, 99, 116, 105, 111, 110, 32, 99, 111, 117, 108, 100, 32, 110, 111, 116, 32, 98, 101, 32, 101, 115, 116, 97, 98, 108, 105, 115, 104, 101, 100};
}
