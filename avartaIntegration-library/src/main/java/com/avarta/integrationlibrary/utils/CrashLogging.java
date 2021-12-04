package com.avarta.integrationlibrary.utils;

import android.content.Context;

import com.bugsnag.android.Bugsnag;


public class CrashLogging {

    public static void LogCrashException(Throwable e) {
        Bugsnag.notify(e);
    }

    public static void initFabric(Context context) {
        Bugsnag.start(context);
    }

    public static void LogCrashException(Throwable e, String message) {
        Bugsnag.addMetadata("INTEGRATION_SDK", "log", message);
        Bugsnag.notify(e);
    }

    public static void setUsernameToCrashLog(Context context, String userName) {
        Bugsnag.setUser("id", "", userName);
    }

}
