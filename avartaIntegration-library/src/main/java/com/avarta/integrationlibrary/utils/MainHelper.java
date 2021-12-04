package com.avarta.integrationlibrary.utils;

import android.content.Context;

public class MainHelper {

    public static String getStringRes(Context context, int resId) {
        if (context == null) {
            return "";
        }

        return context.getString(resId);
    }

    public static String[] getStringArray(Context context, int resId) {
        if (context == null) {
            return null;
        }

        return context.getResources().getStringArray(resId);
    }
}
