package com.avarta.integrationlibrary.utils;

import android.text.TextUtils;
import android.util.Log;

public class Logger {

    private static boolean mIsEnabled;

    public static void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    private static String getClassName(Object object) {
        String mClassName = object.getClass().getName();
        int mFirstPosition = mClassName.lastIndexOf(".") + 1;
        if (mFirstPosition < 0) {
            mFirstPosition = 0;
        }
        mClassName = mClassName.substring(mFirstPosition);
        mFirstPosition = mClassName.lastIndexOf("$");
        if (mFirstPosition > 0) {
            mClassName = mClassName.substring(0, mFirstPosition);
        }
        return mClassName;
    }

    public static void e(Object object, String message) {
        if (mIsEnabled)
            Log.e((object instanceof String) ? (String) object : getClassName(object), message);
    }

    public static void e(Object object, Exception exception) {
        if (mIsEnabled) {
            exception.printStackTrace();
            String logMessage = !TextUtils.isEmpty(exception.getMessage()) ? exception.getMessage() : exception.toString();
            Log.e((object instanceof String) ? (String) object : getClassName(object), logMessage);
        }
    }

    public static void e(Object object, String message, Exception exception) {
        if (mIsEnabled)
            Log.e((object instanceof String) ? (String) object : getClassName(object), message, exception);
    }

    public static void i(Object object, String message) {
        if (mIsEnabled)
            Log.i((object instanceof String) ? (String) object : getClassName(object), message);
    }

    public static void v(Object object, String message) {
        if (mIsEnabled)
            Log.v((object instanceof String) ? (String) object : getClassName(object), message);
    }

    public static void w(Object object, String message) {
        if (mIsEnabled)
            Log.w((object instanceof String) ? (String) object : getClassName(object), message);
    }

    public static void w(Object object, String message, Exception exception) {
        if (mIsEnabled)
            Log.w((object instanceof String) ? (String) object : getClassName(object), message, exception);
    }
}
