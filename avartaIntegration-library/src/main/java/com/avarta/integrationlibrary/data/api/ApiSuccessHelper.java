package com.avarta.integrationlibrary.data.api;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiSuccessHelper {

    private final static String TAG = ApiSuccessHelper.class.getSimpleName();

    // json

    // raw
    public static int getCode(NetworkResponse response) {
        int code;
        try {
            code = response.statusCode;
        } catch (Exception e) {
            code = Const.UNKNOWN_SUCCESS_CODE;
            CrashLogging.LogCrashException(e,"error code : "+ code);
            Logger.e(TAG, e);
        }
        return code;
    }

    public static String getStringData(NetworkResponse response) {
        String msg;
        try {
            msg = new String(response.data);
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            msg = null;
            Logger.e(TAG, e);
        }
        return msg;
    }

    public static boolean getBooleanData(NetworkResponse response) throws Exception {
        String str = getStringData(response);
        if (str == null) throw new Exception();
        return Boolean.parseBoolean(str);
    }

    public static JSONObject getJsonObject(NetworkResponse response) {
        try {
            String str = NetRequest.parseToString(response);

            if (!TextUtils.isEmpty(str)) {
                return new JSONObject(str);
            }
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            Logger.e(TAG, e);
        }

        return new JSONObject();
    }

    public static JSONArray getJsonArray(NetworkResponse response) {
        JSONArray ja;
        try {
            String str = NetRequest.parseToString(response);
            ja = new JSONArray(str);
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            Logger.e(TAG, e);
            ja = new JSONArray();
        }
        return ja;
    }
}