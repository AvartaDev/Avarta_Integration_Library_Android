package com.avarta.integrationlibrary.data.api;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import org.json.JSONObject;

public class ApiErrorHelper {

    private static final String TAG = ApiErrorHelper.class.getSimpleName();

    public static String getErrorMessage(VolleyError error) {
        String msg;
        try {
            byte[] data = error.networkResponse.data;
            String json = new String(data);

            JSONObject jo = new JSONObject(json);
            msg = jo.getString("Message");
        } catch (NullPointerException e) {

            Logger.e("API_ApiErrorHelper", e);
            String message = error.getMessage();
            if (!TextUtils.isEmpty(message) && message.contains("javax.net.ssl.SSLHandshakeException")) {
                msg = Const.SSL_ERROR;
            } else {
                msg = "";
            }
            CrashLogging.LogCrashException(e,msg);
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            Logger.e(TAG, e);
            msg = "";
        }
        return msg;
    }

    public static int getErrorCode(VolleyError error) {
        int code;
        try {
            code = error.networkResponse.statusCode;
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            Logger.e(TAG, e);
            code = Const.UNKNOWN_ERROR_CODE;
        }

        return code;
    }
}
